package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.LeaderboardEntryEntity
import com.example.data.local.entity.TournamentEntity
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.LightCanvas
import com.example.ui.theme.LiveGreen
import com.example.ui.theme.LiveGreenText
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintBorder
import com.example.ui.theme.MutedTextLight
import com.example.ui.theme.SubCardBackground

@Composable
fun LeaderboardScreen(
    tournaments: List<TournamentEntity>,
    selectedTournamentId: Long,
    leaderboardEntries: List<LeaderboardEntryEntity>,
    isLiveFlashing: Boolean,
    onSelectTournament: (Long) -> Unit,
    onSimulateKill: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTourney = tournaments.find { it.id == selectedTournamentId } ?: tournaments.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "REAL-TIME LEADERBOARD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = MutedTextLight
                        )
                    )
                    Text(
                        text = selectedTourney?.title ?: "Pro Circuit S4",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkCanvas
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(LiveGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(LiveGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = LiveGreenText,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }

        // Tournament Selector Tabs
        item {
            if (tournaments.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = tournaments.indexOfFirst { it.id == selectedTournamentId }.coerceAtLeast(0),
                    containerColor = Color.Transparent,
                    contentColor = DarkCanvas,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        val index = tournaments.indexOfFirst { it.id == selectedTournamentId }.coerceAtLeast(0)
                        if (index < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                                height = 3.dp,
                                color = DarkCanvas
                            )
                        }
                    }
                ) {
                    tournaments.forEach { t ->
                        Tab(
                            selected = t.id == selectedTournamentId,
                            onClick = { onSelectTournament(t.id) },
                            text = {
                                Text(
                                    text = t.title,
                                    fontWeight = if (t.id == selectedTournamentId) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            }
                        )
                    }
                }
            }
        }

        // Live Match Ref Simulation Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("simulate_live_kill_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCanvas)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = MintAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Live Score Ref",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "Stream real-time kill feed & placement updates",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MintBorder,
                                fontSize = 11.sp
                            )
                        )
                    }

                    Button(
                        onClick = onSimulateKill,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintAccent,
                            contentColor = DarkCanvas
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("simulate_kill_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Simulate Kill", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Live score flash notification
        item {
            AnimatedVisibility(
                visible = isLiveFlashing,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LiveGreen)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡ LIVE KILL FEED UPDATED - SCOREBOARD RECALCULATED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }

        // Top Podium Summary
        if (leaderboardEntries.size >= 3) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 2nd Place
                    PodiumCard(
                        entry = leaderboardEntries[1],
                        rank = 2,
                        modifier = Modifier.weight(1f)
                    )
                    // 1st Place
                    PodiumCard(
                        entry = leaderboardEntries[0],
                        rank = 1,
                        modifier = Modifier.weight(1.1f)
                    )
                    // 3rd Place
                    PodiumCard(
                        entry = leaderboardEntries[2],
                        rank = 3,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Main Leaderboard Table Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STANDINGS TABLE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MutedTextLight
                    )
                )
                Text(
                    text = "${leaderboardEntries.size} Teams",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = MutedTextLight
                    )
                )
            }
        }

        // Leaderboard List Items
        itemsIndexed(leaderboardEntries) { index, entry ->
            val rankNum = index + 1
            LeaderboardRowItem(
                rank = rankNum,
                entry = entry
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PodiumCard(
    entry: LeaderboardEntryEntity,
    rank: Int,
    modifier: Modifier = Modifier
) {
    val isFirst = rank == 1
    val bgColor = if (isFirst) MintAccent else Color.White
    val borderCol = if (isFirst) MintBorder else CardBorder

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(bgColor)
            .border(1.dp, borderCol, RoundedCornerShape(24.dp))
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(DarkCanvas),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.teamName,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkCanvas
                ),
                maxLines = 1
            )

            Text(
                text = "${entry.kills} Kills | ${entry.booyahs} Booyah",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    color = MutedTextLight
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${entry.totalPoints} pts",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = DarkCanvas
                )
            )
        }
    }
}

@Composable
fun LeaderboardRowItem(
    rank: Int,
    entry: LeaderboardEntryEntity
) {
    val isTop3 = rank <= 3
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (rank == 1) MintAccent.copy(alpha = 0.4f) else SubCardBackground)
            .border(1.dp, if (rank == 1) MintBorder else CardBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("leaderboard_row_$rank")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$rank",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isTop3) DarkCanvas else MutedTextLight
                    ),
                    modifier = Modifier.width(28.dp)
                )

                Column {
                    Text(
                        text = entry.teamName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkCanvas
                        )
                    )
                    Text(
                        text = "${entry.kills} Kills | ${entry.booyahs} Booyah!",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            color = MutedTextLight
                        )
                    )
                }
            }

            Text(
                text = "${entry.totalPoints} pts",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = DarkCanvas
                )
            )
        }
    }
}
