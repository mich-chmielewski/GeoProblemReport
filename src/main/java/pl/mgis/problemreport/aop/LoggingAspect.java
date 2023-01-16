package pl.mgis.problemreport.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("test")
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* pl.mgis.problemreport..*(..))")
    private void anyPublicMethod() {
    }

    @Before("anyPublicMethod()")
    public void beforeAnyPublicMethod(JoinPoint jp) {
        log.info("BEFORE AnyPublicMethod :: {} :: {}", jp.getSignature().getName(), jp.getSourceLocation());
    }


    @After("anyPublicMethod()")
    public void afterAnyPublicMethod(JoinPoint jp) {
        log.info("AFTER AnyPublicMethod :: {} :: {}", jp.getSignature().getName(), jp.getSourceLocation());
    }

    @Around("execution(* pl.mgis.problemreport.controller.api..*(..))")
    public void aroundRestControllerMethod(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.nanoTime();
        log.info("AROUND TIME START:: {} :: logAroundAllRestApiMethods() : {}", startTime, jp.getSignature().getName());
            jp.proceed();
        log.info("AROUND TIME END :: {} :: logAroundAllRestApiMethods() : {}", System.nanoTime() - startTime, jp.getSignature().getName());

    }

    @Around("execution(* pl.mgis.problemreport.controller.template..*(..))")
    public void aroundTemplateControllerMethod(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.nanoTime();
        log.info("AROUND TIME START:: {} :: logAroundAllRestTemplateMethods() : {}", startTime, jp.getSignature().getName());
            jp.proceed();
        log.info("AROUND TIME END :: {} :: logAroundAllRestTemplateMethods() : {}", System.nanoTime() - startTime, jp.getSignature().getName());

    }

}
