package com.braintrain.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.braintrain.app.data.PlayerEntity
import com.braintrain.app.ui.theme.Accent
import com.braintrain.app.ui.theme.FredokaFallback

/**
 * Local device "sign in" — pick an existing profile or create a new one by
 * name. No password, no network: this is what "tied to a user account"
 * means for the local-only persistence option. High scores are stored per
 * username in Room (see PlayerRepository).
 */
@Composable
fun ProfileDialog(
    players: List<PlayerEntity>,
    activeUsername: String?,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {
    var newName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(24.dp),
        ) {
            Text(
                "Choose a player",
                color = Color(0xFF1C1040),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FredokaFallback,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "High scores are saved per player, on this device.",
                color = Color(0x731C1040),
                fontSize = 13.sp,
            )
            Spacer(Modifier.height(20.dp))

            if (players.isNotEmpty()) {
                Text(
                    "EXISTING PLAYERS",
                    color = Color(0x611C1040),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                )
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.heightIn(max = 180.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    items(players, key = { it.username }) { player ->
                        val isCurrent = player.username == activeUsername
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isCurrent) Color(0x1A7C5CFC) else Color(0xFFF5F0FF))
                                .clickable { onSelect(player.username) }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                player.username,
                                color = Color(0xFF1C1040),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FredokaFallback,
                            )
                            if (isCurrent) {
                                Text("Current", color = Accent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            Text(
                "NEW PLAYER",
                color = Color(0x611C1040),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = newName,
                onValueChange = { if (it.length <= 20) newName = it },
                placeholder = { Text("Enter a name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Accent,
                    cursorColor = Accent,
                ),
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = Color(0x731C1040))
                }
                Spacer(Modifier.width(4.dp))
                Button(
                    onClick = { onSelect(newName.trim()) },
                    enabled = newName.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent),
                ) {
                    Text("Continue")
                }
            }
        }
    }
}
