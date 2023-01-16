package pl.mgis.problemreport.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.mgis.problemreport.exception.CustomResponseException;
import pl.mgis.problemreport.model.dto.UserDTO;
import pl.mgis.problemreport.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manager/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable long id) {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getAll(){
        return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> add(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(e->e.getField()+ ": " + e.getDefaultMessage()).collect(Collectors.toList());
            throw new CustomResponseException(errors.toString(),400);
        }
        return new ResponseEntity<>(userService.save(userDTO),HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<UserDTO> edit(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(e->e.getField()+ ": " + e.getDefaultMessage()).collect(Collectors.toList());
            throw new CustomResponseException(errors.toString(),400);
        }
        return new ResponseEntity<>(userService.edit(userDTO),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        userService.delete(id);
    }
}
