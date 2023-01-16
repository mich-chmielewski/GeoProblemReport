package pl.mgis.problemreport.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mgis.problemreport.model.dto.ReportTypeDTO;
import pl.mgis.problemreport.service.ReportTypeService;

import java.util.List;

@RestController
@RequestMapping(value = "/manager/api/reporttype")
public class ReportTypeController {

    private final ReportTypeService reportTypeService;

    public ReportTypeController(ReportTypeService reportTypeService) {
        this.reportTypeService = reportTypeService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ReportTypeDTO> getReportType(@PathVariable("id") long id){
        return ResponseEntity.ok(reportTypeService.get(id));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<ReportTypeDTO>> getAllReportTypes(){
        return new ResponseEntity<>(reportTypeService.getAll(),HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<ReportTypeDTO> addReportType(@RequestBody ReportTypeDTO reportTypeDTO){
        return new ResponseEntity<>(reportTypeService.add(reportTypeDTO),HttpStatus.OK);
    }

    @PutMapping(value = "/")
    public ResponseEntity<ReportTypeDTO> editReportType(@RequestBody ReportTypeDTO reportTypeDTO){
        return new ResponseEntity<>(reportTypeService.edit(reportTypeDTO),HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteReportType(@PathVariable long id) {
        reportTypeService.delete(id);
    }
}
