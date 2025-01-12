package com.example.fakestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.example.fakestore"})
@EnableScheduling
public class FakestoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FakestoreApplication.class, args);
	}

}
