package org.wa55death405.quizhub;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.QuestionAttempt;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.entities.QuizAttempt;
import org.wa55death405.quizhub.repositories.QuestionRepository;
import org.wa55death405.quizhub.repositories.QuizRepository;
import org.wa55death405.quizhub.utils.FakeDataGenerator;
import org.wa55death405.quizhub.utils.FakeDataUtils;

@SpringBootApplication
@RequiredArgsConstructor
public class QuizHubApplication implements CommandLineRunner{

	private final QuestionRepository questionRepository;
	private final EntityManager entityManger;
	private final QuizRepository quizRepository;

	public static void main(String[] args) {
		SpringApplication.run(QuizHubApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
//		QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
//		FakeDataGenerator.fill(quizCreationDTO);
//		Quiz quiz = quizCreationDTO.toEntity(null);
//		quizRepository.save(quiz);
////		entityManger.flush();
//
//		QuizAttempt quizAttempt = new QuizAttempt();
//		quizAttempt.setQuiz(quiz);
//		entityManger.persist(quizAttempt);
//
//		var s = FakeDataUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(quiz);
//		for (var qas : s) {
//			QuestionAttempt qa = qas.toEntity(null);
//			qa.setQuizAttempt(quizAttempt);
//			entityManger.persist(qa);
//		}
	}
}
