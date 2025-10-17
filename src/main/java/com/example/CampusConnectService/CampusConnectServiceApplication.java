package com.example.CampusConnectService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class CampusConnectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusConnectServiceApplication.class, args);
	}

}
