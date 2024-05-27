package org.wa55death405.quizhub;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
public class QuizHubApplication{

	public static void main(String[] args) {
		SpringApplication.run(QuizHubApplication.class, args);
	}

}
