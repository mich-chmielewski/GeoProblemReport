package pl.mgis.problemreport.service;

import org.springframework.stereotype.Service;
import pl.mgis.problemreport.exception.CustomResponseException;
import pl.mgis.problemreport.exception.ResourceNotFoundException;
import pl.mgis.problemreport.mapper.Mapper;
import pl.mgis.problemreport.model.ReportStatus;
import pl.mgis.problemreport.model.Stage;
import pl.mgis.problemreport.model.dto.ReportStatusDTO;
import pl.mgis.problemreport.repository.ReportStatusRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportStatusService {

    private final ReportStatusRepository reportStatusRepository;

    public ReportStatusService(ReportStatusRepository reportStatusRepository) {
        this.reportStatusRepository = reportStatusRepository;
    }

    public ReportStatusDTO findByStatusCode(int statusCode) {

        return reportStatusRepository.findByStatusCode(statusCode)
                .map(Mapper::reportStatusToDto)
                .orElseThrow(() -> new ResourceNotFoundException("No Report Status found"));
    }

    public ReportStatusDTO get(Long id) {
        return Mapper.reportStatusToDto(reportStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Report Status found")));
    }

    public List<ReportStatusDTO> getAll() {
        return reportStatusRepository.findAll()
                .stream()
                .map(Mapper::reportStatusToDto).collect(Collectors.toList());
    }

    /**
     * !! (>ლ)
     * orElse() will always call the given function , regardless of Optional.isPresent()
     * orElseGet() will only call the given function when the Optional.isPresent() == false
     * @return first Waiting Stage StatusDto
     */

    public ReportStatusDTO getFirstStage() {
        return reportStatusRepository.findAll()
                .stream().filter(s -> s.getStage().equals(Stage.WAITING)).findFirst().map(Mapper::reportStatusToDto)
                .orElseGet(()->Mapper
                        .reportStatusToDto(reportStatusRepository
                                .save(new ReportStatus("Waiting", 1, "#FF0000", Stage.WAITING))));

    }

    public ReportStatusDTO save(ReportStatusDTO reportStatusDTO) {
        return Mapper.reportStatusToDto(reportStatusRepository.save(Mapper.reportStatusFromDto(reportStatusDTO)));
    }

    @Transactional
    public ReportStatusDTO edit(ReportStatusDTO reportStatusDTO) {
        ReportStatus reportStatus = reportStatusRepository.findById(reportStatusDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No Report Status found"));
        reportStatus.setStatusCode(reportStatusDTO.getStatusCode());
        reportStatus.setDescription(reportStatusDTO.getDescription());
        reportStatus.setColor(reportStatusDTO.getColor());
        reportStatus.setStage(reportStatusDTO.getStage());
        return reportStatusDTO;
    }

    public void delete(Long id) {
        try {
            reportStatusRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomResponseException(e.getMessage(), 409);
        }
    }
}
