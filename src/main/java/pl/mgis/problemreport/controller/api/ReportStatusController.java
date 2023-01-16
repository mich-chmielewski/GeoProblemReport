package pl.mgis.problemreport.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.mgis.problemreport.exception.CustomResponseException;
import pl.mgis.problemreport.model.dto.ReportStatusDTO;
import pl.mgis.problemreport.service.ReportStatusService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ReportStatusController {

    private final ReportStatusService reportStatusService;

    public ReportStatusController(ReportStatusService reportStatusService) {
        this.reportStatusService = reportStatusService;
    }

    @GetMapping("manager/api/status/{id}")
    public ResponseEntity<ReportStatusDTO> get(@PathVariable Long id){
        return new ResponseEntity<>(reportStatusService.get(id), HttpStatus.OK);
    }

    @GetMapping({"manager/api/status/list","user/api/status/list"})
    public ResponseEntity<List<ReportStatusDTO>> getList(){
        return new ResponseEntity<>(reportStatusService.getAll(), HttpStatus.OK);
    }

    @PostMapping("manager/api/status/")
    public ResponseEntity<ReportStatusDTO> add(@Valid @RequestBody ReportStatusDTO reportStatusDTO,
                                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(e->e.getField()+ ": " + e.getDefaultMessage()).collect(Collectors.toList());
            throw new CustomResponseException(errors.toString(),400);
        }
        return new ResponseEntity<>(reportStatusService.save(reportStatusDTO),HttpStatus.CREATED);
    }

    @PutMapping("manager/api/status/")
    public ResponseEntity<ReportStatusDTO> edit(@Valid @RequestBody ReportStatusDTO reportStatusDTO,
                                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(e->e.getField()+ ": " + e.getDefaultMessage()).collect(Collectors.toList());
            throw new CustomResponseException(errors.toString(),400);
        }
        return new ResponseEntity<>(reportStatusService.edit(reportStatusDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("manager/api/status/{id}")
    public void delete(@PathVariable Long id){
        reportStatusService.delete(id);
    }
}
