package com.ucu.topicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class TopicosApplication {
	public static void main(String[] args) {
		SpringApplication.run(TopicosApplication.class, args);
	}

}