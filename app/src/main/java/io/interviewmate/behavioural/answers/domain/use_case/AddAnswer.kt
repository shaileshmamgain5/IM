package io.interviewmate.behavioural.answers.domain.use_case

import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.domain.model.InvalidAnswerException
import io.interviewmate.behavioural.answers.domain.repository.AnswerRepository

class AddAnswer (
    private val repository: AnswerRepository
        ) {

    @Throws(InvalidAnswerException::class)
    suspend operator fun invoke(answer: Answer) {
        if(answer.duration.equals("")) {
            throw InvalidAnswerException("Answer should be 5s min")
        }
        repository.insertAnswer(answer)
    }
}