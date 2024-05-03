package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JaroslawKopaczTest5Application {

	public static void main(String[] args) {
		SpringApplication.run(JaroslawKopaczTest5Application.class, args);
	}

}
