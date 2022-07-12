package io.interviewmate.behavioural.answers.domain.use_case

import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.domain.repository.AnswerRepository

class DeleteAnswer(
    private val repository: AnswerRepository
) {
    suspend operator fun invoke(answer: Answer) {
        repository.deleteAnswer(answer)
    }
}