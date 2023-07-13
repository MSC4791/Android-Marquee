package personal.msc4791.marquee

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class MarqueeDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

@Composable
fun MarqueeText(
    modifier: Modifier = Modifier,
    textResource: Int,
    direction: MarqueeDirection = MarqueeDirection.LEFT,
    speed: Int = 5000,
    fillParent: Boolean = false
) {
    val frontWidth = remember { mutableStateOf(0.dp) }
    val frontHeight = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val context = LocalContext.current

    val transition = rememberInfiniteTransition()
    val marqueeText = remember { mutableStateOf(context.getString(textResource)) }

    Box(
        modifier = modifier
            .clip(RectangleShape),
        contentAlignment = Alignment.Center
    ) {
        when(direction) {
            MarqueeDirection.UP -> {
                val verticalTransition = transition.animateValue(
                    initialValue = 0.dp,
                    targetValue = frontHeight.value,
                    typeConverter = Dp.VectorConverter,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = speed,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                )
                Text(
                    modifier = Modifier
                        .onGloballyPositioned {
                            frontHeight.value = with(density) {
                                it.size.height.toDp()
                            }
                            if(fillParent) {
                                val copyCount = context.resources.displayMetrics.heightPixels / it.size.height
                                if(copyCount > 2) {
                                    marqueeText.value = marqueeText.value.plus("\n").repeat(copyCount + 1).substringBeforeLast("\n")
                                }
                            }
                        }
                        .offset(0.dp, -verticalTransition.value),
                    text = marqueeText.value
                )
                Text(
                    modifier = Modifier
                        .offset(0.dp, -verticalTransition.value + frontHeight.value),
                    text = marqueeText.value
                )
            }
            MarqueeDirection.DOWN -> {
                val verticalTransition = transition.animateValue(
                    initialValue = 0.dp,
                    targetValue = frontHeight.value,
                    typeConverter = Dp.VectorConverter,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = speed,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                )
                Text(
                    modifier = Modifier
                        .onGloballyPositioned {
                            frontHeight.value = with(density) {
                                it.size.height.toDp()
                            }
                            if(fillParent) {
                                val copyCount = context.resources.displayMetrics.heightPixels / it.size.height
                                if(copyCount > 2) {
                                    marqueeText.value = marqueeText.value.plus("\n").repeat(copyCount + 1).substringBeforeLast("\n")
                                }
                            }
                        }
                        .offset(0.dp, verticalTransition.value),
                    text = marqueeText.value
                )
                Text(
                    modifier = Modifier
                        .offset(0.dp, verticalTransition.value - frontHeight.value),
                    text = marqueeText.value
                )
            }
            MarqueeDirection.LEFT -> {
                val horizontalTransition = transition.animateValue(
                    initialValue = 0.dp,
                    targetValue = frontWidth.value,
                    typeConverter = Dp.VectorConverter,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = speed,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                )
                Text(
                    modifier = Modifier
                        .onGloballyPositioned {
                            frontWidth.value = with(density) {
                                it.size.width.toDp()
                            }
                            if(fillParent) {
                                val copyCount = context.resources.displayMetrics.widthPixels / it.size.width
                                if (copyCount > 2) {
                                    marqueeText.value = marqueeText.value.plus(" ").repeat(copyCount + 1)
                                }
                            }
                        }
                        .offset(horizontalTransition.value),
                    text = marqueeText.value
                )
                Text(
                    modifier = Modifier
                        .offset(horizontalTransition.value - frontWidth.value),
                    text = marqueeText.value
                )
            }
            MarqueeDirection.RIGHT -> {
                val horizontalTransition = transition.animateValue(
                    initialValue = 0.dp,
                    targetValue = frontWidth.value,
                    typeConverter = Dp.VectorConverter,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = speed,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                )
                Text(
                    modifier = Modifier
                        .onGloballyPositioned {
                            frontWidth.value = with(density) {
                                it.size.width.toDp()
                            }
                            if(fillParent) {
                                val copyCount = context.resources.displayMetrics.widthPixels / it.size.width
                                if (copyCount > 2) {
                                    marqueeText.value = marqueeText.value.plus(" ").repeat(copyCount + 1)
                                }
                            }
                        }
                        .offset(-horizontalTransition.value),
                    text = marqueeText.value
                )
                Text(
                    modifier = Modifier
                        .offset(-horizontalTransition.value + frontWidth.value),
                    text = marqueeText.value
                )
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun MarqueeTextPreview() {
    MarqueeText(
        modifier = Modifier
            .fillMaxSize(),
        textResource = R.string.marquee_text,
    )
}