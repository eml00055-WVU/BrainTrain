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
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.braintrain.app.model.AnswerState
import com.braintrain.app.model.OPTION_ACCENT
import com.braintrain.app.model.OPTION_BG
import com.braintrain.app.model.SYNONYM_LEVELS
import com.braintrain.app.ui.components.AnswerTile
import com.braintrain.app.ui.components.ProgressDots
import com.braintrain.app.ui.components.ResultOverlay
import com.braintrain.app.ui.components.ScoreBadge
import com.braintrain.app.ui.components.TimerBadge
import com.braintrain.app.ui.theme.DeepPurple
import com.braintrain.app.ui.theme.FredokaFallback
import com.braintrain.app.ui.theme.Lavender
import kotlinx.coroutines.delay

/** "Find the synonym" mini-game. Direct port of SynonymGame() in App.tsx. */
@Composable
fun SynonymScreen(
    timeLeft: Int,
    showTimeUp: Boolean,
    score: Int,
    onCorrectAnswer: () -> Unit,
    onRoundComplete: () -> Unit,
    onRestart: () -> Unit,
) {
    var levelIndex by remember { mutableStateOf(0) }
    var gameState by remember { mutableStateOf(AnswerState.PLAYING) }
    var selected by remember { mutableStateOf<String?>(null) }
    var wrongOption by remember { mutableStateOf<String?>(null) }
    var showComplete by remember { mutableStateOf(false) }

    val level = SYNONYM_LEVELS[levelIndex]

    fun handleSelect(option: String) {
        if (gameState != AnswerState.PLAYING || showTimeUp || showComplete) return
        selected = option
        if (option == level.answer) {
            gameState = AnswerState.CORRECT
            onCorrectAnswer()
        } else {
            gameState = AnswerState.WRONG
            wrongOption = option
        }
    }

    LaunchedEffect(gameState, levelIndex) {
        when (gameState) {
            AnswerState.CORRECT -> {
                delay(1100)
                if (levelIndex + 1 >= SYNONYM_LEVELS.size) {
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
                    Text(
                        "Q ${levelIndex + 1} / ${SYNONYM_LEVELS.size}",
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
                        "FIND THE SYNONYM FOR",
                        color = Color(0x61FFFFFF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp,
                    )
                    Spacer(Modifier.height(12.dp))
                    Column(
                        modifier = Modifier
                            .sizeIn(minWidth = 220.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0x247C5CFC))
                            .padding(horizontal = 36.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            level.word,
                            color = Color.White,
                            fontSize = 52.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FredokaFallback,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            level.pos,
                            color = Color(0x59FFFFFF),
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                        )
                    }
                }

                ProgressDots(total = SYNONYM_LEVELS.size, currentIndex = levelIndex, activeColor = Color(0xFF6BCB77), dotHeight = 5.dp)
            }

            Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0x0FFFFFFF)))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Lavender)
                    .padding(horizontal = 24.dp, vertical = 22.dp),
            ) {
                Text(
                    "CHOOSE THE SYNONYM",
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
                                val optionFontSize = when {
                                    option.length > 9 -> 16.sp
                                    option.length > 6 -> 18.sp
                                    else -> 22.sp
                                }
                                AnswerTile(
                                    isCorrect = isCorrect,
                                    isWrong = isWrong,
                                    backgroundColor = if (isCorrect) Color(0xFFE8F8EA) else if (isWrong) Color(0xFFFFE8E8) else OPTION_BG[i],
                                    borderColor = if (isCorrect) Color(0xFF6BCB77) else if (isWrong) Color(0xFFFF6B6B) else Color.Transparent,
                                    onClick = { handleSelect(option) },
                                    modifier = Modifier.weight(1f).fillMaxSize(),
                                ) {
                                    Text(
                                        option,
                                        color = if (isCorrect) Color(0xFF2D8C3E) else if (isWrong) Color(0xFFC0392B) else OPTION_ACCENT[i],
                                        fontSize = optionFontSize,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = FredokaFallback,
                                        textAlign = TextAlign.Center,
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
                subtitle = "$score of ${SYNONYM_LEVELS.size} synonyms found",
                accentColor = Color(0xFF6BCB77),
                buttonLabel = "Try Again",
                onButtonClick = onRestart,
            )
        }

        if (showComplete) {
            ResultOverlay(
                emoji = "🎉",
                title = "Wordsmith!",
                subtitle = "All ${SYNONYM_LEVELS.size} synonyms found!",
                accentColor = Color(0xFF6BCB77),
                buttonLabel = "Play Again",
                onButtonClick = onRestart,
            )
        }
    }
}
