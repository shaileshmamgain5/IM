package io.interviewmate.behavioural.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
