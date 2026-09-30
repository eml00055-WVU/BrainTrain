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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.braintrain.app.model.AnswerState
import com.braintrain.app.model.EQUATION_LEVELS
import com.braintrain.app.model.OPTION_ACCENT
import com.braintrain.app.model.OPTION_BG
import com.braintrain.app.ui.components.AnswerTile
import com.braintrain.app.ui.components.ProgressDots
import com.braintrain.app.ui.components.ResultOverlay
import com.braintrain.app.ui.components.ScoreBadge
import com.braintrain.app.ui.components.TimerBadge
import com.braintrain.app.ui.theme.DeepPurple
import com.braintrain.app.ui.theme.FredokaFallback
import com.braintrain.app.ui.theme.Lavender
import kotlinx.coroutines.delay

/** Solve the equation game. */
@Composable
fun EquationScreen(
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
    var selected by remember { mutableStateOf<Int?>(null) }
    var wrongOption by remember { mutableStateOf<Int?>(null) }
    var showComplete by remember { mutableStateOf(false) }

    // Shuffled once per round (reset naturally each time this screen is
    // re-entered, since a fresh round unmounts/remounts the composable).
    val levels = remember { EQUATION_LEVELS.shuffled() }
    val level = levels[levelIndex]

    fun handleSelect(option: Int) {
        if (gameState != AnswerState.PLAYING || showTimeUp || showComplete) return
        selected = option
        if (option == level.answer) {
            gameState = AnswerState.CORRECT
            onCorrectAnswer()
        } else {
            gameState = AnswerState.WRONG
            wrongOption = option
            onWrongAnswer()
        }
    }

    LaunchedEffect(gameState, levelIndex) {
        when (gameState) {
            AnswerState.CORRECT -> {
                delay(1100)
                if (levelIndex + 1 >= EQUATION_LEVELS.size) {
                    showComplete = true
                    onRoundComplete()
                } else {
                    levelIndex += 1
                    gameState = AnswerState.PLAYING
                    selected = null
                    wrongOption = null
                }
            }
            AnswerState.WRONG -> {
                delay(850)
                gameState = AnswerState.PLAYING
                selected = null
                wrongOption = null
            }
            AnswerState.PLAYING -> {}
        }
    }

    val equationFontSize = when {
        level.display.length > 12 -> 30.sp
        level.display.length > 8 -> 36.sp
        else -> 44.sp
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

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
                    androidx.compose.material3.Text(
                        "Q ${levelIndex + 1} / ${EQUATION_LEVELS.size}",
                        color = Color(0x66FFFFFF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FredokaFallback,
                    )
                    TimerBadge(timeLeft)
                    ScoreBadge(score)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.Text(
                        "Solve the equation",
                        color = Color(0x8CFFFFFF),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FredokaFallback,
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        androidx.compose.material3.Text(
                            level.display,
                            color = Color.White,
                            fontSize = equationFontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FredokaFallback,
                        )
                        androidx.compose.material3.Text(
                            "= ?",
                            color = Color(0xFF7C5CFC),
                            fontSize = equationFontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FredokaFallback,
                        )
                    }
                }

                ProgressDots(total = EQUATION_LEVELS.size, currentIndex = levelIndex, activeColor = Color(0xFF7C5CFC), dotHeight = 5.dp)
            }

            Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0x0FFFFFFF)))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Lavender)
                    .padding(horizontal = 24.dp, vertical = 22.dp),
            ) {
                androidx.compose.material3.Text(
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
                    rows.forEachIndexed { rowIndex, rowOptions ->
                        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            rowOptions.forEachIndexed { colIndex, option ->
                                val i = rowIndex * 2 + colIndex
                                val isCorrect = selected == option && gameState == AnswerState.CORRECT
                                val isWrong = option == wrongOption && gameState == AnswerState.WRONG
                                AnswerTile(
                                    isCorrect = isCorrect,
                                    isWrong = isWrong,
                                    backgroundColor = if (isCorrect) Color(0xFFE8F8EA) else if (isWrong) Color(0xFFFFE8E8) else OPTION_BG[i],
                                    borderColor = if (isCorrect) Color(0xFF6BCB77) else if (isWrong) Color(0xFFFF6B6B) else Color.Transparent,
                                    onClick = { handleSelect(option) },
                                    modifier = Modifier.weight(1f).fillMaxSize(),
                                ) {
                                    androidx.compose.material3.Text(
                                        option.toString(),
                                        color = if (isCorrect) Color(0xFF2D8C3E) else if (isWrong) Color(0xFFC0392B) else OPTION_ACCENT[i],
                                        fontSize = 38.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FredokaFallback,
                                    )
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
                title = "Nailed it!",
                subtitle = "All ${levels.size} solved  ·  Score $score",
                accentColor = Color(0xFF7C5CFC),
                buttonLabel = "Play Again",
                onButtonClick = onRestart,
            )
        }
    }
}
