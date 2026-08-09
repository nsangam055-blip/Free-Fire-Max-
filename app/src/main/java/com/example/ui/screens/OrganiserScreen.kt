package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.TeamRegistrationEntity
import com.example.data.local.entity.TournamentEntity
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.LightCanvas
import com.example.ui.theme.LiveGreen
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintBorder
import com.example.ui.theme.MutedTextLight
import com.example.ui.theme.SubCardBackground

@Composable
fun OrganiserScreen(
    tournaments: List<TournamentEntity>,
    registrations: List<TeamRegistrationEntity>,
    onCreateTournament: (String, String, String, String, Int, String) -> Unit,
    onPublishRoom: (Long, String, String) -> Unit,
    onApproveRegistration: (Long, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Squad") }
    var mapName by remember { mutableStateOf("Bermuda") }
    var prizePool by remember { mutableStateOf("₹25,000") }
    var entryFeeStr by remember { mutableStateOf("30") }
    var startTime by remember { mutableStateOf("Today, 09:00 PM IST") }

    var selectedTournamentIdForRoom by remember { mutableStateOf(tournaments.firstOrNull()?.id ?: 1L) }
    var roomIdInput by remember { mutableStateOf("") }
    var roomPassInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Text(
                    text = "ORGANISER INTELLIGENCE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MutedTextLight
                    )
                )
                Text(
                    text = "Host & Room Control",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkCanvas
                    )
                )
            }
        }

        // Host New Tournament Form Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_tournament_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MintAccent)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = DarkCanvas,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Create Free Fire MAX Tournament",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkCanvas
                            )
                        )
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tournament Title") },
                        placeholder = { Text("e.g. Free Fire Bermuda Showdown") },
                        modifier = Modifier.fillMaxWidth().testTag("input_tourney_title"),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = mode,
                            onValueChange = { mode = it },
                            label = { Text("Game Mode") },
                            placeholder = { Text("Squad / Duo / Solo") },
                            modifier = Modifier.weight(1f).testTag("input_tourney_mode"),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = mapName,
                            onValueChange = { mapName = it },
                            label = { Text("Map") },
                            placeholder = { Text("Bermuda / Kalahari") },
                            modifier = Modifier.weight(1f).testTag("input_tourney_map"),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = prizePool,
                            onValueChange = { prizePool = it },
                            label = { Text("Prize Pool") },
                            placeholder = { Text("₹25,000") },
                            modifier = Modifier.weight(1f).testTag("input_tourney_prizepool"),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = entryFeeStr,
                            onValueChange = { entryFeeStr = it },
                            label = { Text("Entry Fee (₹)") },
                            placeholder = { Text("30") },
                            modifier = Modifier.weight(1f).testTag("input_tourney_entryfee"),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                    }

                    Button(
                        onClick = {
                            val fee = entryFeeStr.toIntOrNull() ?: 20
                            if (title.isNotBlank()) {
                                onCreateTournament(title, mode, mapName, prizePool, fee, startTime)
                                title = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("publish_tournament_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkCanvas,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Publish Tournament to App", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Room Dispatcher Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("room_dispatcher_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCanvas)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = MintAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Dispatch Room ID & Password",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = roomIdInput,
                            onValueChange = { roomIdInput = it },
                            label = { Text("Room ID", color = MintBorder) },
                            placeholder = { Text("FFMAX-8841", color = Color.Gray) },
                            modifier = Modifier.weight(1f).testTag("input_room_id"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = MintAccent,
                                unfocusedBorderColor = MintBorder
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = roomPassInput,
                            onValueChange = { roomPassInput = it },
                            label = { Text("Room Pass", color = MintBorder) },
                            placeholder = { Text("991", color = Color.Gray) },
                            modifier = Modifier.weight(1f).testTag("input_room_pass"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = MintAccent,
                                unfocusedBorderColor = MintBorder
                            ),
                            singleLine = true
                        )
                    }

                    Button(
                        onClick = {
                            if (roomIdInput.isNotBlank() && roomPassInput.isNotBlank()) {
                                onPublishRoom(selectedTournamentIdForRoom, roomIdInput, roomPassInput)
                                roomIdInput = ""
                                roomPassInput = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("broadcast_room_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintAccent,
                            contentColor = DarkCanvas
                        )
                    ) {
                        Text("Broadcast Room Access to Players", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Audit Log Header
        item {
            Text(
                text = "PLAYER REGISTRATION AUDIT LOG",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = MutedTextLight
                )
            )
        }

        items(registrations) { reg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("registration_audit_${reg.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = reg.teamName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkCanvas
                            )
                        )
                        Text(
                            text = "UID: ${reg.leaderInGameUid} • UTR: ${reg.utrNumber}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = MutedTextLight
                            )
                        )
                        if (reg.aiVerificationNotes.isNotBlank()) {
                            Text(
                                text = reg.aiVerificationNotes,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    color = LiveGreen
                                )
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (reg.paymentStatus == "VERIFIED") LiveGreen.copy(alpha = 0.2f) else SubCardBackground)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = reg.paymentStatus,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (reg.paymentStatus == "VERIFIED") LiveGreen else DarkCanvas
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
