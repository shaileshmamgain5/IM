package io.interviewmate.behavioural.answers.domain.model

import io.interviewmate.behavioural.util.OrderType

sealed class AnswerOrder(val orderType: OrderType) {
    class Duration(orderType: OrderType): AnswerOrder(orderType)
    class Date(orderType: OrderType): AnswerOrder(orderType)

    fun copy(orderType: OrderType): AnswerOrder {
        return when(this) {
            is Duration -> Duration(orderType)
            is Date -> Date(orderType)
        }
    }
}
