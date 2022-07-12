package io.interviewmate.behavioural.questions.domain.use_case

data class QuestionUseCases(
    val getQuestions: GetQuestions,
    val deleteQuestion: DeleteQuestion,
    val getQuestion: GetQuestion,
    val addQuestion: AddQuestion
)