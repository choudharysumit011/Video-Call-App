package com.ecommerce.videocalling;

import com.ecommerce.videocalling.model.User;
import com.ecommerce.videocalling.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VideoCallingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoCallingApplication.class, args);
	}


}
