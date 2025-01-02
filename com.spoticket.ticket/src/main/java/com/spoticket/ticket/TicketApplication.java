package com.spoticket.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TicketApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicketApplication.class, args);
  }

}
