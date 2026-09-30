package com.braintrain.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.braintrain.app.data.PlayerRepository
import com.braintrain.app.model.PENALTY_PER_WRONG
import com.braintrain.app.model.POINTS_PER_CORRECT
import com.braintrain.app.model.Screen
import com.braintrain.app.model.TIMER_START_SECONDS
import com.braintrain.app.model.speedBonus
import com.braintrain.app.ui.components.ProfileDialog
import com.braintrain.app.ui.screens.EquationScreen
import com.braintrain.app.ui.screens.LeaderboardScreen
import com.braintrain.app.ui.screens.ModeSelectScreen
import com.braintrain.app.ui.screens.ShapePatternScreen
import com.braintrain.app.ui.screens.SynonymScreen
import com.braintrain.app.ui.screens.TitleScreen
import com.braintrain.app.ui.theme.AppBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Root composable. Owns the screen-switching state machine, the shared
 * countdown timer and also the local-profile / high-score
 * layer. Tracks which device profile is signed in, the list of all local profiles
 * (for the leaderboard) and persisting a personal-best score whenever a
 * round ends.
 */
@Composable
fun BrainTrainApp() {
    val context = LocalContext.current
    val repository = remember { PlayerRepository(context) }
    val scope = rememberCoroutineScope()

    val activeUsername by repository.activeUsername.collectAsState(initial = null)
    val allPlayers by repository.allPlayers.collectAsState(initial = emptyList())

    var screen by remember { mutableStateOf(Screen.TITLE) }
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(TIMER_START_SECONDS) }
    var showTimeUp by remember { mutableStateOf(false) }
    var roundComplete by remember { mutableStateOf(false) }

    var showProfileDialog by remember { mutableStateOf(false) }
    var afterLoginAction by remember { mutableStateOf<() -> Unit>({}) }

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

    /* Persist a high score the moment a round ends, whether by finishing
     every level (roundComplete) or by the clock running out (showTimeUp).
     A round that's fully cleared also earns a speed bonus based on how
     much time was left. Finishing faster leaves more time on the clock,
     which means a bigger bonus. */
    LaunchedEffect(showTimeUp, roundComplete) {
        if (!isActiveGame) return@LaunchedEffect
        if (roundComplete) {
            score += speedBonus(timeLeft)
        }
        if (showTimeUp || roundComplete) {
            val username = activeUsername ?: return@LaunchedEffect
            repository.recordScore(username, screen, score)
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

    fun requestLogin(then: () -> Unit) {
        afterLoginAction = then
        showProfileDialog = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
        contentAlignment = Alignment.Center,
    ) {
        when (screen) {
            Screen.TITLE -> TitleScreen(
                activeUsername = activeUsername,
                onPlay = {
                    if (activeUsername == null) {
                        requestLogin { screen = Screen.MODE_SELECT }
                    } else {
                        screen = Screen.MODE_SELECT
                    }
                },
                onLoginClick = { requestLogin {} },
                onLeaderboardClick = { screen = Screen.LEADERBOARD },
            )

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
                onCorrectAnswer = { score += POINTS_PER_CORRECT },
                onWrongAnswer = { score = (score - PENALTY_PER_WRONG).coerceAtLeast(0) },
                onRoundComplete = { roundComplete = true },
                onRestart = ::restart,
            )

            Screen.EQUATIONS -> EquationScreen(
                timeLeft = timeLeft,
                showTimeUp = showTimeUp,
                score = score,
                onCorrectAnswer = { score += POINTS_PER_CORRECT },
                onWrongAnswer = { score = (score - PENALTY_PER_WRONG).coerceAtLeast(0) },
                onRoundComplete = { roundComplete = true },
                onRestart = ::restart,
            )

            Screen.SYNONYMS -> SynonymScreen(
                timeLeft = timeLeft,
                showTimeUp = showTimeUp,
                score = score,
                onCorrectAnswer = { score += POINTS_PER_CORRECT },
                onWrongAnswer = { score = (score - PENALTY_PER_WRONG).coerceAtLeast(0) },
                onRoundComplete = { roundComplete = true },
                onRestart = ::restart,
            )

            Screen.LEADERBOARD -> LeaderboardScreen(
                players = allPlayers,
                activeUsername = activeUsername,
                onBack = { screen = Screen.TITLE },
            )
        }

        if (showProfileDialog) {
            ProfileDialog(
                players = allPlayers,
                activeUsername = activeUsername,
                onDismiss = { showProfileDialog = false },
                onSelect = { username ->
                    scope.launch {
                        repository.signIn(username)
                        showProfileDialog = false
                        afterLoginAction()
                        afterLoginAction = {}
                    }
                },
            )
        }
    }
}
