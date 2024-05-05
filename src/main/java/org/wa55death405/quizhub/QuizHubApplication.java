package org.wa55death405.quizhub;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.utils.FakeDataUtils;

@SpringBootApplication
@RequiredArgsConstructor
public class QuizHubApplication implements CommandLineRunner{

	private final FakeDataUtils fakeDataUtils;

	public static void main(String[] args) {
		SpringApplication.run(QuizHubApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Quiz quiz = fakeDataUtils.fillFakeQuizData();
	}
}
