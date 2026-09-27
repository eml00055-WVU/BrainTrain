package com.braintrain.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.braintrain.app.model.ShapeName
import com.braintrain.app.model.ShapeIcon
import com.braintrain.app.ui.theme.DeepPurple
import com.braintrain.app.ui.theme.FredokaFallback
import com.braintrain.app.ui.theme.Lavender

private data class GameModeInfo(
    val label: String,
    val description: String,
    val accent: Color,
    val accentBg: Color,
    val onPress: () -> Unit,
)

@Composable
fun ModeSelectScreen(
    onBack: () -> Unit,
    onSelectShapes: () -> Unit,
    onSelectEquations: () -> Unit,
    onSelectSynonyms: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // ── TOP: header ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DeepPurple)
                .padding(horizontal = 24.dp, vertical = 40.dp),
        ) {
            Text(
                text = "← Back",
                color = Color(0x73FFFFFF),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FredokaFallback,
                modifier = Modifier.clickable { onBack() },
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Choose a Game",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FredokaFallback,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "What will you train today?",
                color = Color(0x66FFFFFF),
                fontSize = 14.sp,
            )
        }

        // Hairline divider
        Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0x0FFFFFFF)))

        // ── BOTTOM: mode cards ───────────────────────────────────────
        val modes = listOf(
            GameModeInfo(
                label = "Shape Patterns",
                description = "Spot the sequence · pick what comes next",
                accent = Color(0xFF7C5CFC),
                accentBg = Color(0xFFEDE8FF),
                onPress = onSelectShapes,
            ),
            GameModeInfo(
                label = "Equation Solver",
                description = "Solve the equation · pick the right answer",
                accent = Color(0xFF4D96FF),
                accentBg = Color(0xFFE2EEFF),
                onPress = onSelectEquations,
            ),
            GameModeInfo(
                label = "Synonyms",
                description = "Find the word that means the same thing",
                accent = Color(0xFF6BCB77),
                accentBg = Color(0xFFE8F8EA),
                onPress = onSelectSynonyms,
            ),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Lavender)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            modes.forEachIndexed { i, mode ->
                ModeCard(mode, index = i)
            }
        }
    }
}

@Composable
private fun ModeCard(mode: GameModeInfo, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .clickable { mode.onPress() }
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(mode.accentBg),
            contentAlignment = Alignment.Center,
        ) {
            ModeIcon(index)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = mode.label,
                color = Color(0xFF1C1040),
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FredokaFallback,
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = mode.description,
                color = Color(0x731C1040),
                fontSize = 13.sp,
            )
        }

        Text("›", color = mode.accent, fontSize = 20.sp)
    }
}

@Composable
private fun ModeIcon(index: Int) {
    when (index) {
        0 -> Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            ShapeIcon(ShapeName.SQUARE, size = 18.dp)
            ShapeIcon(ShapeName.CIRCLE, size = 18.dp)
            ShapeIcon(ShapeName.TRIANGLE, size = 18.dp)
        }
        1 -> Text(
            "3×4+?",
            color = Color(0xFF4D96FF),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FredokaFallback,
        )
        else -> Text(
            "word\n≈ word",
            color = Color(0xFF6BCB77),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FredokaFallback,
            lineHeight = 15.sp,
        )
    }
}
