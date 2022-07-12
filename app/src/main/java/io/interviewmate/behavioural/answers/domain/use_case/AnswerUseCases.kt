package io.interviewmate.behavioural.answers.domain.use_case

data class AnswerUseCases(
    val getAnswer: GetAnswer,
    val getAnswers: GetAnswers,
    val getAnswersFor: GetAnswersFor,
    val deleteAnswer: DeleteAnswer,
    val addAnswer: AddAnswer
)