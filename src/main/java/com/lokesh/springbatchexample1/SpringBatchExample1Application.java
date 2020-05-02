package com.lokesh.springbatchexample1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBatchExample1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchExample1Application.class, args);
	}
}
