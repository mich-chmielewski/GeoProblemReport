package pl.mgis.problemreport.controller.template;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Controller
public class ErrorTemplate implements ErrorController {

    @GetMapping("/error")
    public String showErrorPage(Model model, HttpServletRequest request) {
        model.addAttribute("code",  request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
        return "error";
    }
}
