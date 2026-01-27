package com.personalfinancetracker.personalfinancetracker.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
public class LoggingAspect {
    private final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void allServiceMethods(){

    }

    @Around("allServiceMethods()")
    public Object logMethodCalls(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object res = null;
        String methodName = jp.getSignature().getName();
        String className = jp.getSignature().getDeclaringType().getSimpleName();
        Object[] args = jp.getArgs();
        LOGGER.info("Entering: "+ className+"."+methodName+" | "+"Arguments are: "+ Arrays.toString(args));
        try{
            jp.proceed();
        }
        catch (Exception e){
            LOGGER.error("Exception in  {}.{} | Error Message: {}", className, methodName, e.getMessage());
            throw e;
        }

        long duration = System.currentTimeMillis() - startTime;

        LOGGER.info("Exiting: {}.{} | Arguments: {} | Execution Time: {}", className, methodName, Arrays.toString(args), duration);



        return res;
    }
}
