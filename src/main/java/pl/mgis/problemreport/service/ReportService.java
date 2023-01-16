package pl.mgis.problemreport.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.mgis.problemreport.config.MvcConfiguration;
import pl.mgis.problemreport.exception.CustomResponseException;
import pl.mgis.problemreport.exception.CustomerEmailNotFoundException;
import pl.mgis.problemreport.exception.ResourceNotFoundException;
import pl.mgis.problemreport.mapper.Mapper;
import pl.mgis.problemreport.model.Report;
import pl.mgis.problemreport.model.ReportStatus;
import pl.mgis.problemreport.model.ReportType;
import pl.mgis.problemreport.model.dto.*;
import pl.mgis.problemreport.repository.CommentRepository;
import pl.mgis.problemreport.repository.ReportRepository;
import pl.mgis.problemreport.repository.ReportStatusRepository;
import pl.mgis.problemreport.repository.ReportTypeRepository;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final EmailService emailService;

    public ReportService(ReportRepository reportRepository, CommentRepository commentRepository,
                         ReportTypeRepository reportTypeRepository, ReportStatusRepository reportStatusRepository,
                         EmailService emailService) {
        this.reportRepository = reportRepository;
        this.commentRepository = commentRepository;
        this.reportTypeRepository = reportTypeRepository;
        this.reportStatusRepository = reportStatusRepository;
        this.emailService = emailService;
    }

    public ReportDTO get(long id) {
        return Mapper.reportToDto(reportRepository.findById(id)
                .orElseThrow(() -> new CustomResponseException("Not existing Report Type id", 404)));
    }

    public List<ReportDTO> getAllPaged(Pageable page) {
        return reportRepository.findAll(page).stream().map(Mapper::reportToDto).collect(Collectors.toList());
    }

    public List<ReportDTO> getAll(int page, int size, Sort.Direction sortDirection) {
        return reportRepository.findAllReports(PageRequest.of(page, size, Sort.by(sortDirection, "id"))).stream().map(Mapper::reportToDto).collect(Collectors.toList());
    }

    public List<ReportGjFeature> getAllGjFeature() {
        return reportRepository.findAll().stream().map(Mapper::reportToGeoJson).collect(Collectors.toList());
    }

    public ReportDTO saveForm(ReportDTO reportDTO) {
        Report report = Mapper.reportFromDto(reportDTO);
        ReportStatus reportStatus = reportStatusRepository.findByStatusCode(reportDTO.getReportStatusDTO().getStatusCode())
                .orElseThrow(() -> new CustomResponseException("Not existing Status code", 404));
        report.setReportStatus(reportStatus);
        ReportType reportType = reportTypeRepository.findById(reportDTO.getreportTypeDTO().getId())
                .orElseThrow(() -> new CustomResponseException("Not existing Report Type id", 404));
        report.setReportType(reportType);
        report.setPhotoPath(saveImageFromBase64File(reportDTO.getPhoto()));
        return Mapper.reportToDto(reportRepository.save(report));
    }

    public List<ReportWithCommentsDTO> getAllWithComments(Pageable page) {

        List<ReportWithCommentsDTO> reportWithCommentsDTOList = reportRepository.findAll(page)
                .stream().map(Mapper::reportWithCommentsToDTO).collect(Collectors.toList());

        List<Long> ids = reportWithCommentsDTOList.stream().map(ReportWithCommentsDTO::getId).collect(Collectors.toList());

        List<CommentDTO> allByReportIdIn = commentRepository.findAllUsingReportIds(ids).stream()
                .map(Mapper::commentToDto).collect(Collectors.toList());

        reportWithCommentsDTOList.forEach(r -> r.setComments(
                        allByReportIdIn.stream().filter(c -> c.getReportId() == r.getId()
                        ).collect(Collectors.toList())
                )
        );
        return reportWithCommentsDTOList;
    }

    public ReportDTO add(ReportSimpleDTO reportSimpleDTO) {
        Report report = Mapper.reportFromAddDto(reportSimpleDTO);
        ReportStatus reportStatus = reportStatusRepository.findByStatusCode(reportSimpleDTO.getReportStatusId())
                .orElseThrow(() -> new CustomResponseException("Not existing Status code", 404));
        report.setReportStatus(reportStatus);
        ReportType reportType = reportTypeRepository.findById(reportSimpleDTO.getReportTypeId())
                .orElseThrow(() -> new CustomResponseException("Not existing Report Type id", 404));
        report.setReportType(reportType);
        return Mapper.reportToDto(reportRepository.save(report));
    }

    @Transactional
    public ReportDTO editReport(ReportSimpleDTO reportSimpleDTO) {
        Report report = reportRepository.findById(reportSimpleDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        ReportStatus reportStatus = reportStatusRepository.findByStatusCode(reportSimpleDTO.getReportStatusId())
                .orElseThrow(() -> new CustomResponseException("Not existing Status code", 404));
        report.setReportStatus(reportStatus);
        ReportType reportType = reportTypeRepository.findById(reportSimpleDTO.getReportTypeId())
                .orElseThrow(() -> new CustomResponseException("Not existing Report Type id", 404));
        report.setReportType(reportType);
        report.setMessage(reportSimpleDTO.getMessage());
        report.setEmail(reportSimpleDTO.getEmail());
        report.setLon(reportSimpleDTO.getLon());
        report.setLat(reportSimpleDTO.getLat());
        return Mapper.reportToDto(report);
    }

    public void delete(long id) {
        Report reportToRemove = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        try {
            if (!Files.deleteIfExists(Paths.get(MvcConfiguration.FILES_DIR + reportToRemove.getPhotoPath()))) {
                log.info("File {} not deleted", reportToRemove.getPhotoPath());
            }
        } catch (IOException e) {
            log.info("Errors while deleting file. {}", e.getMessage());
        }
        reportRepository.deleteById(id);
    }

    @Transactional
    public void deleteEmailInReports(String uuid) throws CustomerEmailNotFoundException {
        Report report = reportRepository.findReportByUuid(uuid)
                .orElseThrow(() -> new CustomerEmailNotFoundException("No matching reports found for given uuid"));
        if (report.getEmail() == null) {
            throw new CustomerEmailNotFoundException("No matching email found for given uuid");
        }
        reportRepository.findAllReportsByEmail(report.getEmail()).forEach(r -> r.setEmail(null));
    }

    public ReportDataTableDTO dataTableReports(int start, int length, int draw, String order,
                                               String sortColName, String searchText) {
        int totalRecords = reportRepository.totalReports();
        int page = start / length;
        String finalSearchText = (searchText.equals("")) ? null : searchText.toLowerCase();
        ReportDataTableDTO reportDataTableDTO = new ReportDataTableDTO();
        reportDataTableDTO.setData(
                reportRepository.findReportsByString(finalSearchText, PageRequest.of(page, length, Sort.by(Sort.Direction.fromString(order), sortColName)))
                        .stream()
                        .map(Mapper::reportToDto)
                        .collect(Collectors.toList())
        );
        reportDataTableDTO.setRecordsFiltered(totalRecords);
        reportDataTableDTO.setRecordsTotal(totalRecords); // Needed to show Pagination in Datatable
        reportDataTableDTO.setDraw(draw);
        return reportDataTableDTO;
    }

    @Transactional
    public void changeStatus(long id, int statusCode) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        ReportStatus reportStatus = reportStatusRepository.findByStatusCode(statusCode)
                .orElseThrow(() -> new ResourceNotFoundException("Report Status not found"));
        report.setReportStatus(reportStatus);
        ReportDTO reportDTO = Mapper.reportToDto(report);
        emailService.sendInformationReportMessage(reportDTO, new CommentDTO());
    }

    public List<Map<String, Integer>> getReportsStatistic() {
        List<Report> allReports = reportRepository.findAll();
        List<ReportType> allTypes = reportTypeRepository.findAll();
        List<ReportStatus> allStatus = reportStatusRepository.findAll();

        Map<String, Integer> typesStatisticMap = new TreeMap<>();
        for (ReportType type : allTypes) {
            typesStatisticMap.put(type.getDescription(), (int) allReports.stream()
                    .filter(r -> r.getReportType().getId() == type.getId()).count()
            );
        }

        Map<String, Integer> statusStatisticMap = new TreeMap<>();
        for (ReportStatus reportStatus : allStatus) {
            statusStatisticMap.put(reportStatus.getDescription(), (int) allReports.stream()
                    .filter(s -> s.getReportStatus().getStatusCode() == reportStatus.getStatusCode()).count());
        }

        return Arrays.asList(typesStatisticMap, statusStatisticMap);
    }

    private String saveImageFromBase64File(String imageBase64String) {
        if (imageBase64String == null || imageBase64String.equals("") || !Base64.isBase64(imageBase64String)) {
            return null;
        }
        String fileName = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(imageBase64String));
            String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
            fileName = "i" + new Date().getTime() + "." + mimeType.split("/")[1];
            Path destinationFile = Paths.get(MvcConfiguration.FILES_DIR, fileName);
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.info("Errors while saving image file. {}", e.getMessage());
        }
        return fileName;
    }

}
