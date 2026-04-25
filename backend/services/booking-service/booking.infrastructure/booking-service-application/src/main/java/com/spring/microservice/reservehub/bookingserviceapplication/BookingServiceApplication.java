package com.spring.microservice.reservehub.bookingserviceapplication;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.spring.microservice.reservehub.bookingserviceapplication",
        "com.spring.microservice.reservehub.port",
        "com.spring.microservice.reservehub"
})
//@EntityScan(basePackages = "com.spring.microservice.reservehub")
@EntityScan(basePackages = "com.spring.microservice.reservehub.entity")
@EnableJpaRepositories(basePackages = "com.spring.microservice.reservehub.bookingserviceapplication.persistence")
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }

}
