package org.wa55death405.quizhub.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.repositories.*;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final AnswerAttemptRepository answerAttemptRepository;
    private final QuestionAttemptRepository questionAttemptRepository;
    private final QuestionService questionService;


    public Integer createFakeQuiz() {
        // create quiz
        Quiz quiz = new Quiz();
        quiz.setTitle("New Quiz");
        quizRepository.save(quiz);

        //  create questions
        Question question1 = new Question();
        question1.setQuestion("What is the capital of France?");
        question1.setQuestionType(QuestionType.SHORT_ANSWER);
        question1.setQuiz(quiz);
        questionRepository.save(question1);

        // create answers
        Answer answer1 = new Answer();
        answer1.setAnswer("Paris");
        answer1.setQuestion(question1);
        answerRepository.save(answer1);

        // create quiz attempt
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setQuiz(quiz);
        quizAttemptRepository.save(quizAttempt);

        // create question attempt
        QuestionAttempt questionAttempt1 = new QuestionAttempt();
        questionAttempt1.setQuestion(question1);
        questionAttempt1.setQuizAttempt(quizAttempt);
        questionAttemptRepository.save(questionAttempt1);

        // create answer attempt
        AnswerAttempt answerAttempt1 = new AnswerAttempt();
        answerAttempt1.setAnswer("Paris");
        answerAttempt1.setQuestionAttempt(questionAttempt1);
        answerAttemptRepository.save(answerAttempt1);

        return quiz.getId();
    }

    public QuizAttempt processQuizAttempt(Integer quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new RuntimeException("Quiz attempt not found")
        );
        return processQuizAttempt(quizAttempt);
    }

    public QuizAttempt processQuizAttempt(QuizAttempt quizAttempt) {
        if (quizAttempt == null) return null;
        Set<QuestionAttempt> questionAttempts = quizAttempt.getQuestionAttempts();
        for (QuestionAttempt questionAttempt : questionAttempts){
            questionService.handleQuestionAttempt(questionAttempt);
        }

        float score = QuizAttempt.calculateScore(quizAttempt);
        quizAttempt.setScore(score);
        quizAttemptRepository.save(quizAttempt);

        return quizAttempt;
    }
}
