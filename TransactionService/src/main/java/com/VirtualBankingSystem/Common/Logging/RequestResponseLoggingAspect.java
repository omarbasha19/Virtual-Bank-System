package com.VirtualBankingSystem.Common.Logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestResponseLoggingAspect {

    @Autowired
    private LoggingService loggingService;

    // Pointcut that matches all public methods in controller classes
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
    }

    @Before("controllerMethods() && args(request,..)")
    public void logBefore(JoinPoint joinPoint, Object request) {
        loggingService.logRequest(request);
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "response")
    public void logAfterReturning(JoinPoint joinPoint, Object response) {
        loggingService.logResponse(response);
    }
}
