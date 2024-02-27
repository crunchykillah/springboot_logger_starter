package com.technokratos.loggerstarter.aspect;

import com.technokratos.loggerstarter.config.LoggerStarterConfiguration;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Aspect
public class LoggerAspect {
    private final Logger log = LoggerFactory.getLogger(LoggerAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void logForController() {
    }

    @Pointcut("@annotation(com.technokratos.loggerstarter.annotation.TestAnnotation)")
    public void logForTestAnnotation() {
    }

    @After("(logForController() || logForTestAnnotation())")
    public void logAfterController(JoinPoint joinPoint) {
        log.info("LOG AFTER: Method {}", joinPoint.getSignature().getName());
    }

    @Before("(logForController() || logForTestAnnotation())")
    public void logBeforeController(JoinPoint joinPoint) {
        log.info("LOG BEFORE: Method {}", joinPoint.getSignature().getName());
    }

    @Around("(logForController() || logForTestAnnotation())")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Enter: {}.{}() with argument's = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);
            return result;
        } catch (Throwable e) {
            log.error("Exception in {}.{}()", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e);
            throw e;
        }
    }
}
