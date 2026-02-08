package com.cryptodemoaccount.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MessageEventHandlerLogger {

    @Before("execution(* com.cryptodemoaccount.events.MessageEventHandler.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log.info("call message handler method: [{}] with args={}", joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @After("execution(* com.cryptodemoaccount.events.MessageEventHandler.*(..))")
    public void logAfterMethod(JoinPoint joinPoint) {
        log.info("end of execution message handler method: [{}] with args={}", joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }
}
