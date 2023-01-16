package pl.mgis.problemreport.security.basic;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Profile("dev")
@RestController
public class LogoutController {

    @GetMapping("/logout")
    void logout(HttpServletRequest req) throws ServletException {
        req.logout();
    }
}
