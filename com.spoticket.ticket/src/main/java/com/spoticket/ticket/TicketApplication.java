package com.spoticket.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;


@EnableFeignClients
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class TicketApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicketApplication.class, args);
  }

}
