package com.casaApuestas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class CasaApuestasSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasaApuestasSpringBootApplication.class, args);
	}

}
