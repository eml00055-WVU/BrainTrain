package com.braintrain.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.braintrain.app.model.Screen
import com.braintrain.app.model.TIMER_START_SECONDS
import com.braintrain.app.ui.screens.EquationScreen
import com.braintrain.app.ui.screens.ModeSelectScreen
import com.braintrain.app.ui.screens.ShapePatternScreen
import com.braintrain.app.ui.screens.SynonymScreen
import com.braintrain.app.ui.screens.TitleScreen
import com.braintrain.app.ui.theme.AppBackground
import kotlinx.coroutines.delay

/**
 * Root composable. Owns the screen-switching state machine and the shared
 * countdown timer
 */
@Composable
fun BrainTrainApp() {
    var screen by remember { mutableStateOf(Screen.TITLE) }
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(TIMER_START_SECONDS) }
    var showTimeUp by remember { mutableStateOf(false) }
    var roundComplete by remember { mutableStateOf(false) }

    val isActiveGame = screen == Screen.SHAPES || screen == Screen.EQUATIONS || screen == Screen.SYNONYMS

    // Tick down once per second while a game is active
    LaunchedEffect(isActiveGame, showTimeUp, roundComplete) {
        if (!isActiveGame) return@LaunchedEffect
        while (!showTimeUp && !roundComplete && timeLeft > 0) {
            delay(1000)
            timeLeft -= 1
        }
        if (timeLeft <= 0 && !roundComplete) {
            showTimeUp = true
        }
    }

    fun startGame(target: Screen) {
        score = 0
        timeLeft = TIMER_START_SECONDS
        showTimeUp = false
        roundComplete = false
        screen = target
    }

    fun restart() {
        showTimeUp = false
        roundComplete = false
        score = 0
        timeLeft = TIMER_START_SECONDS
        screen = Screen.MODE_SELECT
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
        contentAlignment = Alignment.Center,
    ) {
        when (screen) {
            Screen.TITLE -> TitleScreen(onPlay = { screen = Screen.MODE_SELECT })

            Screen.MODE_SELECT -> ModeSelectScreen(
                onBack = { screen = Screen.TITLE },
                onSelectShapes = { startGame(Screen.SHAPES) },
                onSelectEquations = { startGame(Screen.EQUATIONS) },
                onSelectSynonyms = { startGame(Screen.SYNONYMS) },
            )

            Screen.SHAPES -> ShapePatternScreen(
                timeLeft = timeLeft,
                showTimeUp = showTimeUp,
                score = score,
                onCorrectAnswer = { score += 1 },
                onRoundComplete = { roundComplete = true },
                onRestart = ::restart,
            )

            Screen.EQUATIONS -> EquationScreen(
                timeLeft = timeLeft,
                showTimeUp = showTimeUp,
                score = score,
                onCorrectAnswer = { score += 1 },
                onRoundComplete = { roundComplete = true },
                onRestart = ::restart,
            )

            Screen.SYNONYMS -> SynonymScreen(
                timeLeft = timeLeft,
                showTimeUp = showTimeUp,
                score = score,
                onCorrectAnswer = { score += 1 },
                onRoundComplete = { roundComplete = true },
                onRestart = ::restart,
            )
        }
    }
}
