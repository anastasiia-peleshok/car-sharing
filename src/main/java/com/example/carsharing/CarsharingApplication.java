package com.example.carsharing;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CarsharingApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		String dbUsername = dotenv.get("DB_USERNAME");
		String dbPassword = dotenv.get("DB_PASSWORD");
		String dbDriver = dotenv.get("DB_DRIVER");
		String dbUrl = dotenv.get("DB_URL");
		String jwtSecret = dotenv.get("JWT_SECRET");

		System.setProperty("DB_USERNAME", dbUsername);
		System.setProperty("DB_PASSWORD", dbPassword);
		System.setProperty("DB_DRIVER", dbDriver);
		System.setProperty("DB_URL", dbUrl);
		System.setProperty("JWT_SECRET", jwtSecret);
		SpringApplication.run(CarsharingApplication.class, args);
	}

}
