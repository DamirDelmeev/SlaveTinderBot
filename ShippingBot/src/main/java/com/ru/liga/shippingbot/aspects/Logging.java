package com.ru.liga.shippingbot.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class Logging {
    @AfterThrowing(pointcut = "execution(* com.ru.liga.shippingbot.bot.BotMassage.*(..))", throwing = "exception")
    public void allMethodsBotMassage(Throwable exception) {
        log.info("log message: {}", exception.getMessage());
    }

    @AfterThrowing(pointcut = "execution(* com.ru.liga.shippingbot.entity.PersonModel.*(..))", throwing = "exception")
    public void allMethodsPersonModel(Throwable exception) {
        log.info("log message: {}", exception.getMessage());
    }

    @AfterThrowing(pointcut = "execution(* com.ru.liga.shippingbot.handlers.MessageHandler.*(..))", throwing = "exception")
    public void allMethodsMessageHandler(Throwable exception) {
        log.info("log message: {}", exception.getMessage());
    }
}
