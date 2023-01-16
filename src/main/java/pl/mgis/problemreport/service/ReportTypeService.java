package pl.mgis.problemreport.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.mgis.problemreport.exception.CustomResponseException;
import pl.mgis.problemreport.mapper.Mapper;
import pl.mgis.problemreport.model.ReportType;
import pl.mgis.problemreport.model.dto.ReportTypeDTO;
import pl.mgis.problemreport.repository.ReportTypeRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportTypeService {
    private final ReportTypeRepository reportTypeRepository;

    public ReportTypeService(ReportTypeRepository reportTypeRepository) {
        this.reportTypeRepository = reportTypeRepository;
    }

    public ReportTypeDTO get(long id) {
        return Mapper.reportTypeToDto(reportTypeRepository.findById(id).orElseThrow());
    }

    @Cacheable("ReportTypesDictionary")
    public List<ReportTypeDTO> getAll() {
        return reportTypeRepository.findAll().stream().map(Mapper::reportTypeToDto).collect(Collectors.toList());
    }

    @CacheEvict(value="ReportTypesDictionary", allEntries=true)
    public ReportTypeDTO add(ReportTypeDTO reportTypeDTO) {
        ReportType reportType = reportTypeRepository.save(Mapper.reportTypeFromDto(reportTypeDTO));
        return Mapper.reportTypeToDto(reportType);
    }

    @CacheEvict(value="ReportTypesDictionary", allEntries=true)
    @Transactional
    public ReportTypeDTO edit(ReportTypeDTO reportTypeDTO) {
        ReportType reportTypeEdited = reportTypeRepository.findById(reportTypeDTO.getId()).orElseThrow();
        reportTypeEdited.setActive(reportTypeDTO.isActive());
        reportTypeEdited.setDescription(reportTypeDTO.getDescription());
        reportTypeEdited.setEmail(reportTypeDTO.getEmail());
        return Mapper.reportTypeToDto(reportTypeRepository.save(reportTypeEdited));
    }

    @CacheEvict(value="ReportTypesDictionary", allEntries=true)
    public String delete(long id) {
        try{
            reportTypeRepository.deleteById(id);
            return "ok!";
        } catch(Exception e){
           throw new CustomResponseException(e.getMessage(), 409);
        }
    }
}
