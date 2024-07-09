package org.krainet.tracker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "Tracker system",
        description = "For krainet trainee position",
        contact = @Contact(name = "Lisavy Stanislau",
                url = "https://github.com/yumeinaruu",
                email = "stas.lisavoy@icloud.com")
))
@SpringBootApplication
public class TrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackerApplication.class, args);
    }

}
