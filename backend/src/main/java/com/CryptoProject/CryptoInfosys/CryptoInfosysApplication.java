package com.CryptoProject.CryptoInfosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoInfosysApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoInfosysApplication.class, args);
	}

}
