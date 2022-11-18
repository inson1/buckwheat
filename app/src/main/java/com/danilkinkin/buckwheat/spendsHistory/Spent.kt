package com.danilkinkin.buckwheat.spendsHistory

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danilkinkin.buckwheat.base.Collapse
import com.danilkinkin.buckwheat.base.DeleteState
import com.danilkinkin.buckwheat.base.SwipeForDismiss
import com.danilkinkin.buckwheat.base.rememberSwipeForDismiss
import com.danilkinkin.buckwheat.data.entities.Spent
import com.danilkinkin.buckwheat.ui.BuckwheatTheme
import com.danilkinkin.buckwheat.ui.colorEditor
import com.danilkinkin.buckwheat.ui.colorOnEditor
import com.danilkinkin.buckwheat.util.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Spent(
    spent: Spent,
    currency: ExtendCurrency,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeableState = rememberSwipeForDismiss()

    Collapse(
        show = !spent.deleted,
        onHide = {
            coroutineScope.launch {
                swipeableState.animateTo(DeleteState.IDLE, anim = TweenSpec(0))
            }
        },
    ) {
        SwipeForDismiss(
            modifier = Modifier.fillMaxWidth(),
            swipeableState = swipeableState,
            contentColor = colorEditor,
            onSwiped = {
                onDelete()
            }
        ) {
            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier
                        .padding(
                            start = 32.dp,
                            top = 16.dp,
                            bottom = 16.dp,
                        )
                        .weight(1f)
                ) {
                    Text(
                        text = prettyCandyCanes(spent.value, currency = currency),
                        style = MaterialTheme.typography.displaySmall,
                        color = colorOnEditor,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier,
                    )
                    if (spent.comment.isNotEmpty()) {
                        Text(
                            text = spent.comment,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorOnEditor,
                            softWrap = true,
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                ),
                        )
                    }
                }
                Box(Modifier) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(horizontal = 32.dp, vertical = 16.dp),
                        text = prettyDate(spent.date),
                        style = MaterialTheme.typography.labelSmall,
                        color = colorOnEditor,
                        softWrap = false,
                    )
                }
            }
        }
    }
}

@Preview(name = "Default")
@Composable
private fun PreviewDefault() {
    BuckwheatTheme {
        Spent(
            Spent(value = BigDecimal(12340), date = Date()),
            ExtendCurrency(type = CurrencyType.NONE)
        )
    }
}

@Preview(name = "Night mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNightMode() {
    BuckwheatTheme {
        Spent(
            Spent(value = BigDecimal(12340), date = Date(), comment = "Comment for spent"),
            ExtendCurrency(type = CurrencyType.NONE)
        )
    }
}

@Preview(name = "With big spent and long comment (Night mode)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewWithBigSpentAndLongCommentNightMode() {
    BuckwheatTheme {
        Spent(
            Spent(
                value = BigDecimal(123456789009876543),
                date = Date(),
                comment = "Very loooong comment for veryyy loooooooooooooooooong spent. And yet row for more length",
            ),
            ExtendCurrency(type = CurrencyType.NONE)
        )
    }
}

@Preview(name = "Small screen", widthDp = 220)
@Composable
private fun PreviewSmallScreen() {
    BuckwheatTheme {
        Spent(
            Spent(value = BigDecimal(12340), date = Date()),
            ExtendCurrency(type = CurrencyType.NONE)
        )
    }
}