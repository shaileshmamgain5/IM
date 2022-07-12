package io.interviewmate.behavioural.questions.presentation.questions.components
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.interviewmate.behavioural.questions.domain.model.QuestionOrder
import io.interviewmate.behavioural.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    questionOrder: QuestionOrder = QuestionOrder.Date(OrderType.Descending),
    onOrderChange: (QuestionOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = questionOrder is QuestionOrder.Title,
                onSelect = { onOrderChange(QuestionOrder.Title(questionOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = questionOrder is QuestionOrder.Date,
                onSelect = { onOrderChange(QuestionOrder.Date(questionOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = questionOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(questionOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = questionOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(questionOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}