package pl.mgis.problemreport.controller;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.mgis.problemreport.exception.CustomResponseException;
import pl.mgis.problemreport.exception.ListCustomResponseException;
import pl.mgis.problemreport.exception.ResourceNotFoundException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

@ApiIgnore
@ControllerAdvice
@Order(value = 2)
public class ExceptionHandlerController extends Exception {

    //@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, String> exceptionResponse = new HashMap<>();
        exceptionResponse.put("code", String.valueOf(HttpStatus.NOT_FOUND.value()));
        exceptionResponse.put("date", String.valueOf(new Date()));
        exceptionResponse.put("message", ex.getMessage());
        exceptionResponse.put("description", request.getDescription(true));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomResponseException.class)
    public ResponseEntity<Object> customResponseException(CustomResponseException ex, WebRequest request) {
        Map<String, String> exceptionResponse = new HashMap<>();
        exceptionResponse.put("code", String.valueOf(ex.getHttpCode()));
        exceptionResponse.put("date", String.valueOf(new Date()));
        exceptionResponse.put("message", ex.getMessage());
        exceptionResponse.put("description", request.getDescription(true));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.valueOf(ex.getHttpCode()));
    }

    @ExceptionHandler(ListCustomResponseException.class)
    public ResponseEntity<List<Map<String, String>>> customResponseExceptionList(ListCustomResponseException exList, WebRequest request) {
        List<Map<String, String>> exceptionResponseList = new ArrayList<>();
        for (CustomResponseException ex: exList.getCustomResponseExceptionList()){
            Map<String, String> exceptionResponse = new HashMap<>();
            exceptionResponse.put("code", String.valueOf(ex.getHttpCode()));
            exceptionResponse.put("date", String.valueOf(new Date()));
            exceptionResponse.put("message", ex.getMessage());
            exceptionResponse.put("description", request.getDescription(false));
            exceptionResponseList.add(exceptionResponse);
        }
        return new ResponseEntity<>(exceptionResponseList, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandler(Exception ex, WebRequest request) {
        Map<String, String> exceptionResponse = new HashMap<>();
        exceptionResponse.put("code", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        exceptionResponse.put("date", String.valueOf(new Date()));
        exceptionResponse.put("message", ex.getMessage());
        exceptionResponse.put("description", request.getDescription(true));
        ex.printStackTrace();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
