package com.braintrain.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.braintrain.app.ui.theme.FredokaFallback

/** Formats seconds as M:SS*/
fun formatTimer(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "$m:${s.toString().padStart(2, '0')}"
}

/** Creates timer. Change color based on time left.*/
@Composable
fun TimerBadge(timeLeft: Int, modifier: Modifier = Modifier) {
    val color = when {
        timeLeft <= 10 -> Color(0xFFFF6B6B)
        timeLeft <= 15 -> Color(0xFFFFB627)
        else -> Color(0xB3FFFFFF)
    }
    val bg = when {
        timeLeft <= 10 -> Color(0x26FF6B6B)
        timeLeft <= 15 -> Color(0x1FFFB627)
        else -> Color(0x14FFFFFF)
    }
    val pulse = timeLeft <= 10

    val transition = rememberInfiniteTransition(label = "timerPulse")
    val animatedScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = if (pulse) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "timerScale",
    )
    val scale = if (pulse) animatedScale else 1f

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("⏱", fontSize = 12.sp)
        Spacer(Modifier.width(4.dp))
        Text(
            text = formatTimer(timeLeft),
            color = color,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FredokaFallback,
            modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale),
        )
    }
}

@Composable
fun ScoreBadge(score: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0x1AFFFFFF))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("★", color = Color(0xFFFFD93D), fontSize = 13.sp)
        Spacer(Modifier.width(4.dp))
        Text(
            text = score.toString(),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FredokaFallback,
        )
    }
}

/** Progress dots row — active dot widens, completed dots turn green. */
@Composable
fun ProgressDots(
    total: Int,
    currentIndex: Int,
    activeColor: Color,
    modifier: Modifier = Modifier,
    completedColor: Color = Color(0xFF6BCB77),
    idleColor: Color = Color(0x24FFFFFF),
    dotHeight: Dp = 5.dp,
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(total) { i ->
            val color = when {
                i == currentIndex -> activeColor
                i < currentIndex -> completedColor
                else -> idleColor
            }
            val width = if (i == currentIndex) 18.dp else dotHeight
            Box(
                modifier = Modifier
                    .height(dotHeight)
                    .width(width)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}

/**
 * A single answer tile.
 */
@Composable
fun AnswerTile(
    isCorrect: Boolean,
    isWrong: Boolean,
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val shakeX = remember { Animatable(0f) }
    val popScale = remember { Animatable(1f) }

    LaunchedEffect(isWrong) {
        if (isWrong) {
            shakeX.snapTo(0f)
            shakeX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 380
                    0f at 0
                    -9f at 76
                    9f at 152
                    -9f at 228
                    9f at 304
                    0f at 380
                },
            )
        }
    }

    LaunchedEffect(isCorrect) {
        if (isCorrect) {
            popScale.snapTo(1f)
            popScale.animateTo(
                targetValue = 1f,
                animationSpec = keyframes {
                    durationMillis = 350
                    1f at 0
                    1.15f at 175
                    1f at 350
                },
            )
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
            .clickable(enabled = enabled) { onClick() }
            .graphicsLayer(translationX = shakeX.value)
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.graphicsLayer(scaleX = popScale.value, scaleY = popScale.value)) {
            content()
        }
    }
}
