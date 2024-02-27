package com.technokratos.loggerstarter.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.technokratos.loggerstarter.aspect.LoggerAspect;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        prefix = "logger.aspect",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false)
@RequiredArgsConstructor
public class LoggerStarterConfiguration {

    @Bean
    public LoggerAspect loggerAspect() {
        return new LoggerAspect();
    }


    @Bean
    @ConditionalOnMissingBean(Logger.class)
    public Logger logger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(LoggerAspect.class);
        logger.addAppender(consoleAppender);
        logger.setLevel(Level.ALL);
        logger.setAdditive(true);

        return logger;
    }
}
