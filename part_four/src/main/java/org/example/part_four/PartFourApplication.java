package org.example.part_four;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.logging.Logger;

@SpringBootApplication
public class PartFourApplication {
    @Value("${app.welcome}")
    private String startUpMessage;

    private static final Logger logger = Logger.getLogger(PartFourApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(PartFourApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logStartupMessage(){
        logger.info(startUpMessage);
    }
}
