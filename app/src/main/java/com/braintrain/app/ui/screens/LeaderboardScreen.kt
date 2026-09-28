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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.braintrain.app.data.PlayerEntity
import com.braintrain.app.ui.theme.DeepPurple
import com.braintrain.app.ui.theme.FredokaFallback
import com.braintrain.app.ui.theme.Lavender

private data class LeaderboardTab(val label: String, val color: Color, val score: (PlayerEntity) -> Int)

private val TABS = listOf(
    LeaderboardTab("Shapes", Color(0xFF7C5CFC)) { it.shapesBest },
    LeaderboardTab("Equations", Color(0xFF4D96FF)) { it.equationsBest },
    LeaderboardTab("Synonyms", Color(0xFF6BCB77)) { it.synonymsBest },
)

/** Local, on-device leaderboard across every profile that has signed in. */
@Composable
fun LeaderboardScreen(
    players: List<PlayerEntity>,
    activeUsername: String?,
    onBack: () -> Unit,
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tab = TABS[tabIndex]

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DeepPurple)
                .padding(horizontal = 24.dp, vertical = 40.dp),
        ) {
            Text(
                "← Back",
                color = Color(0x73FFFFFF),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FredokaFallback,
                modifier = Modifier.clickable { onBack() },
            )
            Spacer(Modifier.height(24.dp))
            Text(
                "Leaderboard",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FredokaFallback,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Best scores on this device",
                color = Color(0x66FFFFFF),
                fontSize = 14.sp,
            )
        }

        Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0x0FFFFFFF)))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Lavender)
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TABS.forEachIndexed { i, t ->
                    TabChip(
                        label = t.label,
                        selected = i == tabIndex,
                        accent = t.color,
                        onClick = { tabIndex = i },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            val ranked = players
                .map { it to tab.score(it) }
                .filter { it.second > 0 }
                .sortedByDescending { it.second }

            if (ranked.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        "No scores yet — be the first!",
                        color = Color(0x611C1040),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    itemsIndexed(ranked, key = { _, item -> item.first.username }) { index, (player, score) ->
                        LeaderboardRow(
                            rank = index + 1,
                            name = player.username,
                            score = score,
                            isYou = player.username == activeUsername,
                            accent = tab.color,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabChip(label: String, selected: Boolean, accent: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) accent else Color.White)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            color = if (selected) Color.White else Color(0xFF1C1040),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FredokaFallback,
        )
    }
}

@Composable
private fun LeaderboardRow(rank: Int, name: String, score: Int, isYou: Boolean, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (isYou) accent.copy(alpha = 0.12f) else Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(if (rank <= 3) accent else Color(0x0F1C1040)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                rank.toString(),
                color = if (rank <= 3) Color.White else Color(0x731C1040),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FredokaFallback,
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                name,
                color = Color(0xFF1C1040),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FredokaFallback,
            )
            if (isYou) {
                Text("You", color = accent, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }

        Text(
            score.toString(),
            color = accent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FredokaFallback,
        )
    }
}
