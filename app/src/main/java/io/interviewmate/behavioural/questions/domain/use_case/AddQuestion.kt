package io.interviewmate.behavioural.questions.domain.use_case

import io.interviewmate.behavioural.questions.domain.model.InvalidQuestionException
import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.repository.QuestionRepository

class AddQuestion (
    private val repository: QuestionRepository
        ) {

    @Throws(InvalidQuestionException::class)
    suspend operator fun invoke(question: Question) {
        if(question.title.isBlank()) {
            throw InvalidQuestionException("Question text can't be empty")
        }

        repository.insertQuestion(question)
    }
}