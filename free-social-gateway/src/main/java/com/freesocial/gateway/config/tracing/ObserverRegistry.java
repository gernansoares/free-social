package com.freesocial.gateway.config.tracing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ObserverRegistry {

    /*
    @Bean
    ApplicationListener<ApplicationStartedEvent> doOnStart(ObservationRegistry registry) {
        return event -> generateString(registry);
    }

    public void generateString(ObservationRegistry registry) {
        String something = Observation
                .createNotStarted("server.job", registry)    //1
                .lowCardinalityKeyValue("jobType", "string")    //2
                //3
                .observe(() -> {
                    log.info("Generating a String...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return "NOTHING";
                    }
                    return "SOMETHING";
                });

        log.info("Result was: " + something);
    }
    */

}
