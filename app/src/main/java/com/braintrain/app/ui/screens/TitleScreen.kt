package com.braintrain.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.braintrain.app.model.FLOATERS
import com.braintrain.app.model.ShapeName
import com.braintrain.app.model.ShapeIcon
import com.braintrain.app.ui.theme.DeepPurple
import com.braintrain.app.ui.theme.FredokaFallback
import com.braintrain.app.ui.theme.Lavender

@Composable
fun TitleScreen(
    activeUsername: String?,
    onPlay: () -> Unit,
    onLoginClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // ── TOP: hero ────────────────────────────────────────────────
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(DeepPurple)
                .padding(horizontal = 28.dp, vertical = 32.dp),
            contentAlignment = Alignment.Center,
        ) {
            val w = this.maxWidth
            val h = this.maxHeight

            FLOATERS.forEach { f ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = w * f.xPercent, top = h * f.yPercent)
                ) {
                    ShapeIcon(name = f.shape, size = f.size.dp)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Icon cluster
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    listOf(ShapeName.SQUARE, ShapeName.CIRCLE, ShapeName.TRIANGLE).forEach { s ->
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x14FFFFFF)),
                            contentAlignment = Alignment.Center,
                        ) {
                            ShapeIcon(name = s, size = 30.dp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x477C5CFC)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("?", color = Color(0xFF7C5CFC), fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FredokaFallback)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "BrainTrain",
                    color = Color.White,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FredokaFallback,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Can you crack the pattern?",
                    color = Color(0x73FFFFFF),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }

        // Hairline divider
        Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0x0FFFFFFF)))

        // ── BOTTOM: buttons ──────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Lavender)
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            PrimaryButton(text = "▶  Play", onClick = onPlay)
            Spacer(Modifier.height(16.dp))
            if (activeUsername != null) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(
                        "Playing as ",
                        color = Color(0x731C1040),
                        fontSize = 14.sp,
                    )
                    Text(
                        activeUsername,
                        color = Color(0xFF1C1040),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FredokaFallback,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Switch player",
                    color = Color(0xFF7C5CFC),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onLoginClick() },
                )
                Spacer(Modifier.height(12.dp))
            } else {
                SecondaryButton(text = "Login", onClick = onLoginClick)
                Spacer(Modifier.height(16.dp))
            }
            GhostButton(text = "🏆  Leaderboards", onClick = onLeaderboardClick)
        }
    }
}

@Composable
private fun PrimaryButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF7C5CFC))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, fontFamily = FredokaFallback)
    }
}

@Composable
private fun SecondaryButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Transparent)
            .then(Modifier)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Color(0xFF7C5CFC), fontSize = 20.sp, fontWeight = FontWeight.SemiBold, fontFamily = FredokaFallback)
    }
}

@Composable
private fun GhostButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x0F1C1040))
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Color(0xFF1C1040), fontSize = 20.sp, fontWeight = FontWeight.SemiBold, fontFamily = FredokaFallback)
    }
}
