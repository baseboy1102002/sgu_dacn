package com.sgu.schedulerApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
public class SchedulerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerAppApplication.class, args);
	}

}
