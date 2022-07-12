package io.interviewmate.behavioural.questions.domain.use_case

import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.repository.QuestionRepository

class DeleteQuestion (
    private val repository: QuestionRepository
        ) {
    suspend operator fun invoke(question: Question) {
        repository.deleteQuestion(question)
    }
}