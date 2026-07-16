package com.ayush.waypoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WaypointApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaypointApplication.class, args);
	}

}
