package com.braintrain.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.braintrain.app.model.AnswerState
import com.braintrain.app.model.SHAPE_LEVELS
import com.braintrain.app.model.ShapeIcon
import com.braintrain.app.model.ShapeName
import com.braintrain.app.ui.components.ProgressDots
import com.braintrain.app.ui.components.ResultOverlay
import com.braintrain.app.ui.components.ScoreBadge
import com.braintrain.app.ui.components.TimerBadge
import com.braintrain.app.ui.components.AnswerTile
import com.braintrain.app.ui.theme.DeepPurple
import com.braintrain.app.ui.theme.FredokaFallback
import com.braintrain.app.ui.theme.Lavender
import kotlinx.coroutines.delay

/**
 * Shape pattern matching game.
 */
@Composable
fun ShapePatternScreen(
    timeLeft: Int,
    showTimeUp: Boolean,
    score: Int,
    onCorrectAnswer: () -> Unit,
    onWrongAnswer: () -> Unit,
    onRoundComplete: () -> Unit,
    onRestart: () -> Unit,
) {
    var levelIndex by remember { mutableStateOf(0) }
    var gameState by remember { mutableStateOf(AnswerState.PLAYING) }
    var selected by remember { mutableStateOf<ShapeName?>(null) }
    var wrongShape by remember { mutableStateOf<ShapeName?>(null) }
    var showComplete by remember { mutableStateOf(false) }

    // Shuffled once per round (reset naturally each time this screen is
    // re-entered, since a fresh round unmounts/remounts the composable).
    val levels = remember { SHAPE_LEVELS.shuffled() }
    val level = levels[levelIndex]

    fun handleSelect(shape: ShapeName) {
        if (gameState != AnswerState.PLAYING || showTimeUp || showComplete) return
        selected = shape
        if (shape == level.answer) {
            gameState = AnswerState.CORRECT
            onCorrectAnswer()
        } else {
            gameState = AnswerState.WRONG
            wrongShape = shape
            onWrongAnswer()
        }
    }

    // Advance / clear feedback after a short delay.
    LaunchedEffect(gameState, levelIndex) {
        when (gameState) {
            AnswerState.CORRECT -> {
                delay(1100)
                if (levelIndex + 1 >= SHAPE_LEVELS.size) {
                    showComplete = true
                    onRoundComplete()
                } else {
                    levelIndex += 1
                    gameState = AnswerState.PLAYING
                    selected = null
                    wrongShape = null
                }
            }
            AnswerState.WRONG -> {
                delay(850)
                gameState = AnswerState.PLAYING
                selected = null
                wrongShape = null
            }
            AnswerState.PLAYING -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TOP: pattern panel ─────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(DeepPurple)
                    .padding(horizontal = 35.dp, vertical = 52.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "LEVEL ${levelIndex + 1} / ${SHAPE_LEVELS.size}",
                        color = Color(0x66FFFFFF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FredokaFallback,
                    )
                    TimerBadge(timeLeft)
                    ScoreBadge(score)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "What comes next?",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FredokaFallback,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Spot the pattern · pick the shape",
                        color = Color(0x73FFFFFF),
                        fontSize = 14.sp,
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    level.pattern.forEach { shape ->
                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x12FFFFFF)),
                            contentAlignment = Alignment.Center,
                        ) {
                            ShapeIcon(shape, size = 36.dp)
                        }
                    }
                    // Answer tile
                    val answerBg = when (gameState) {
                        AnswerState.CORRECT -> Color(0x386BCB77)
                        AnswerState.WRONG -> Color(0x38FF6B6B)
                        AnswerState.PLAYING -> Color(0x387C5CFC)
                    }
                    val answerBorder = when (gameState) {
                        AnswerState.CORRECT -> Color(0xFF6BCB77)
                        AnswerState.WRONG -> Color(0xFFFF6B6B)
                        AnswerState.PLAYING -> Color(0xFF7C5CFC)
                    }
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(answerBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        when {
                            gameState == AnswerState.CORRECT && selected != null ->
                                ShapeIcon(selected!!, size = 36.dp)
                            gameState == AnswerState.WRONG ->
                                Text("✕", color = Color(0xFFFF6B6B), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                            else ->
                                Text("?", color = Color(0xFF7C5CFC), fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = FredokaFallback)
                        }
                    }
                }

                ProgressDots(total = SHAPE_LEVELS.size, currentIndex = levelIndex, activeColor = Color(0xFF7C5CFC), dotHeight = 6.dp)
            }

            Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0x0FFFFFFF)))

            // ── BOTTOM: answer grid ─────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Lavender)
                    .padding(horizontal = 24.dp, vertical = 22.dp),
            ) {
                Text(
                    "CHOOSE YOUR ANSWER",
                    color = Color(0x611C1040),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))

                val rows = level.options.chunked(2)
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    rows.forEach { rowShapes ->
                        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            rowShapes.forEach { shape ->
                                val isCorrect = selected == shape && gameState == AnswerState.CORRECT
                                val isWrong = shape == wrongShape && gameState == AnswerState.WRONG
                                AnswerTile(
                                    isCorrect = isCorrect,
                                    isWrong = isWrong,
                                    backgroundColor = if (isCorrect) Color(0xFFE8F8EA) else if (isWrong) Color(0xFFFFE8E8) else shape.bg,
                                    borderColor = if (isCorrect) Color(0xFF6BCB77) else if (isWrong) Color(0xFFFF6B6B) else Color.Transparent,
                                    onClick = { handleSelect(shape) },
                                    modifier = Modifier.weight(1f).fillMaxSize(),
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        ShapeIcon(shape, size = 50.dp)
                                        Text(
                                            shape.label,
                                            color = if (isCorrect) Color(0xFF2D8C3E) else if (isWrong) Color(0xFFC0392B) else Color(0xFF1C1040),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = FredokaFallback,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showTimeUp && !showComplete) {
            ResultOverlay(
                emoji = "⏰",
                title = "Time's Up!",
                subtitle = "$levelIndex of ${levels.size} solved  ·  Score $score",
                accentColor = Color(0xFF7C5CFC),
                buttonLabel = "Try Again",
                onButtonClick = onRestart,
            )
        }

        if (showComplete) {
            ResultOverlay(
                emoji = "🎉",
                title = "You nailed it!",
                subtitle = "All ${levels.size} solved  ·  Score $score",
                accentColor = Color(0xFF7C5CFC),
                buttonLabel = "Play Again",
                onButtonClick = onRestart,
            )
        }
    }
}
