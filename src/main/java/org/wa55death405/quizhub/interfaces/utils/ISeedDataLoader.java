package org.wa55death405.quizhub.interfaces.utils;

import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;

import java.util.List;

/*
    * This interface is used to load seed data.
    * The data is loaded from a JSON file.
    * The data can be used to populate the database with some initial data.
    * Or it can be used to test the application that might want more human readable data.
 */

public interface ISeedDataLoader {
    /*
        * This method is used to get the list of QuizCreationDTOs.
        * The QuizCreationDTOs are used to create quizzes.
     */
    List<QuizCreationDTO> getQuizCreationDTOs();

}
