package com.httpclient.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.httpclient" })
@SpringBootApplication
public class CustomHttpClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomHttpClientApplication.class, args);
	}

}
