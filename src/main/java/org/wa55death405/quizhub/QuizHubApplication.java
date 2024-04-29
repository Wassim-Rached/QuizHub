package org.wa55death405.quizhub;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wa55death405.quizhub.services.QuizService;

@SpringBootApplication
@RequiredArgsConstructor
public class QuizHubApplication implements CommandLineRunner{

	private final QuizService quizService;

	public static void main(String[] args) {
		SpringApplication.run(QuizHubApplication.class, args);

	}


	@Override
	public void run(String... args) throws Exception {
		Integer id = quizService.createFakeQuiz();
		System.out.println("FAKE QUIZ CREATED WITH ID: " + id);
	}
}
