package com.ferrinsa.fairpartner;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info =
@Info(
        title = "FairPartner REST API",
        description = "Backend for the FairPartner application.",
        version = "1.0",
        contact = @Contact(email = "ifersa19@gmail.com", name = "Ferrinsa")))
public class FairPartnerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FairPartnerApiApplication.class, args);
    }

}
