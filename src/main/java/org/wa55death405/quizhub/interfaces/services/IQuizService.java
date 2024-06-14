package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.dto.StandardPageList;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.entities.QuizAttempt;

import java.util.List;
import java.util.UUID;

/*
    this interface contains all the methods
    that the api will need related to the quiz
*/

public interface IQuizService {
    /*
        * search quizzes by title
        * if title is null or empty, return all quizzes
        * @param title the title to search for
        * @return a list of quizzes that match the title
     */
    StandardPageList<QuizGeneralInfoDTO> searchQuizzes(String title, int page, int size);

    /*
        * create a new quiz
        * @param quizCreationDTO the data needed to create the quiz
        * @return the created quiz
     */
    Quiz createQuiz(QuizCreationDTO quizCreationDTO);

    /*
        * get a quiz by its id
        * @param quizId the id of the quiz
        * @return the quiz with the given id
     */
    QuizAttempt startQuizAttempt(UUID quizId);

    /*
        * get a quiz attempt by its id
        * @param quizAttemptId id of the quiz attempt
        * @return the quiz attempt with the given id
     */
    QuizAttemptTakingDTO getQuizAttemptTaking(UUID quizAttemptId);

    /*
        * submit the question attempts of a quiz attempt
        * @param questionAttemptSubmissions the question attempts to submit
        * @param quizAttemptId the id of the quiz attempt
     */
    void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptSubmissions, UUID quizAttemptId);

    /*
        * cancel a quiz attempt
        * @param quizAttemptId the id of the quiz attempt
     */
    void cancelQuizAttempt(UUID quizAttemptId);

    /*
        * finish a quiz attempt
        * @param quizAttemptId the id of the quiz attempt
        * @return the finished quiz attempt
     */
    QuizAttempt finishQuizAttempt(UUID quizAttemptId);

    /*
        * get the result of a quiz attempt
        * @param quizAttemptId id of the quiz attempt
        * @return the result of the quiz attempt
     */
    QuizAttemptResultDTO getQuizAttemptResult(UUID quizAttemptId);

    /*
        * get the info needed to create a quiz
        * @param quizId the id of the quiz
        * @return the needed information to create the quiz
     */
    QuizCreationDTO getQuizCreationInfo(UUID quizId);

    /*
        * get the quiz by its id
        * @param quizId the id of the quiz
        * @return the quiz with the given id
     */
    QuizGeneralInfoDTO getQuizById(UUID quizId);
}