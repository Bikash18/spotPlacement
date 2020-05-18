package com.smartPark.spotPlacement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories({"com.smartPark.spotPlacement.repository"})
public class SpotPlacementApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpotPlacementApplication.class, args);
	}

}
