//package pl.mgis.problemreport.logging;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class RequestLoggerFilter implements Filter {
//    private static final Logger logger = LoggerFactory.getLogger(RequestLoggerFilter.class);
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        if (servletRequest instanceof HttpServletRequest) {
//            HttpServletRequest req = (HttpServletRequest) servletRequest;
//            logger.info("[ReqLoggerFilter] " + req.getRequestURI() + "; " + req.getRemoteAddr());
//        }
//        if (servletResponse instanceof HttpServletResponse) {
//            HttpServletResponse resp = (HttpServletResponse) servletResponse;
//            logger.info("[RespLoggerFilter] " + resp.getStatus() + "; " + resp.getContentType());
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
