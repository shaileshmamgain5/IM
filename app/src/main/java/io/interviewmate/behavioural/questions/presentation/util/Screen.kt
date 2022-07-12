package io.interviewmate.behavioural.questions.presentation.util

sealed class Screen(val route: String) {
    object QuestionsScreen: Screen("questions_screen")
    object AddEditQuestionScreen: Screen("add_edit_question_screen")
    object AnswersScreen: Screen("answers_screen")
}