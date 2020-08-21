package com.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2 // ip:port/swagger-ui.html
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
