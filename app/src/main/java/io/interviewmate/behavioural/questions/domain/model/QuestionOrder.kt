package io.interviewmate.behavioural.questions.domain.model

import io.interviewmate.behavioural.util.OrderType

sealed class QuestionOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): QuestionOrder(orderType)
    class Date(orderType: OrderType): QuestionOrder(orderType)

    fun copy(orderType: OrderType): QuestionOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }
    }
}
