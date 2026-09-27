package com.braintrain.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.braintrain.app.ui.theme.FredokaFallback

/**
 * Full-screen overlay shown when a level/round is finished, used for both
 * the "Time's Up" and "Completion" states across all three mini-games.
 */
@Composable
fun ResultOverlay(
    emoji: String,
    title: String,
    subtitle: String,
    accentColor: Color,
    buttonLabel: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xF0140A32)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(emoji, fontSize = 64.sp)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FredokaFallback,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = subtitle,
                    color = Color(0x8CFFFFFF),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(accentColor)
                    .clickable { onButtonClick() }
                    .padding(horizontal = 44.dp, vertical = 14.dp),
            ) {
                Text(
                    text = buttonLabel,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FredokaFallback,
                )
            }
        }
    }
}
