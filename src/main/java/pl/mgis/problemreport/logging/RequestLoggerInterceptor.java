//package pl.mgis.problemreport.logging;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class RequestLoggerInterceptor implements HandlerInterceptor {
//
//    private static final Logger logger = LoggerFactory.getLogger(RequestLoggerInterceptor.class);
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        logger.info("[pre Interceptor]:" + request.getRequestURI() +" ;" + request.getMethod());
//        return true;
//    }
//
//
//}
