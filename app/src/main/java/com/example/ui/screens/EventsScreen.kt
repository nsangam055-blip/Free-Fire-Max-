package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.TournamentEntity
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.LightCanvas
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintBorder
import com.example.ui.theme.MutedTextLight
import com.example.ui.theme.SubCardBackground

@Composable
fun EventsScreen(
    tournaments: List<TournamentEntity>,
    onSelectTournamentToPay: (TournamentEntity) -> Unit,
    onViewTournamentDetail: (TournamentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Squad", "Duo", "Solo")

    val filteredTournaments = remember(tournaments, selectedFilter) {
        if (selectedFilter == "All") tournaments
        else tournaments.filter { it.gameMode.equals(selectedFilter, ignoreCase = true) }
    }

    val featuredTournament = tournaments.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header
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
                        text = "ORGANISED BY SANGAM",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = MutedTextLight
                        )
                    )
                    Text(
                        text = "FREE FIRE MAX",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = DarkCanvas
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MintAccent)
                        .border(1.dp, MintBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "FF",
                        fontWeight = FontWeight.Black,
                        color = DarkCanvas,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Poster Highlights Badge Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SubCardBackground)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🏆 Prizes", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp))
                Text(text = "🛡️ Fair Play", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp))
                Text(text = "⚡ Smooth", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp))
                Text(text = "🔴 Live Stream", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp))
            }
        }

        // Categories Grid based on User Poster
        item {
            Column {
                Text(
                    text = "TOURNAMENT CATEGORIES (BY SANGAM)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MutedTextLight
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Duo Group Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("category_duo_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkCanvas)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("DUO GROUP", style = MaterialTheme.typography.labelSmall.copy(color = MintAccent, fontWeight = FontWeight.Bold, fontSize = 9.sp))
                            Text("₹200", style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Black))
                            Text("ENTRY FEE", style = MaterialTheme.typography.labelSmall.copy(color = MintBorder, fontSize = 8.sp))
                        }
                    }

                    // Single Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("category_solo_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MintAccent)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("SINGLE", style = MaterialTheme.typography.labelSmall.copy(color = DarkCanvas, fontWeight = FontWeight.Bold, fontSize = 9.sp))
                            Text("₹150", style = MaterialTheme.typography.titleLarge.copy(color = DarkCanvas, fontWeight = FontWeight.Black))
                            Text("ENTRY FEE", style = MaterialTheme.typography.labelSmall.copy(color = MutedTextLight, fontSize = 8.sp))
                        }
                    }

                    // Squad (4 Person) Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("category_squad_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkCanvas)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("SQUAD (4)", style = MaterialTheme.typography.labelSmall.copy(color = MintAccent, fontWeight = FontWeight.Bold, fontSize = 9.sp))
                            Text("₹500", style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Black))
                            Text("ENTRY FEE", style = MaterialTheme.typography.labelSmall.copy(color = MintBorder, fontSize = 8.sp))
                        }
                    }
                }
            }
        }

        // Hero Prize Pool Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(MintAccent)
                    .border(1.dp, MintBorder, RoundedCornerShape(32.dp))
                    .testTag("hero_banner_card")
            ) {
                // Background hero image with subtle overlay
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Tournament Hero Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop,
                    alpha = 0.25f
                )

                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.85f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = featuredTournament?.status ?: "Registration Open",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkCanvas
                                )
                            )
                        }

                        Text(
                            text = "04:12:01",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = DarkCanvas
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = featuredTournament?.prizePool ?: "₹50,000",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkCanvas
                        )
                    )

                    Text(
                        text = "Grand Prize Pool • Free Fire MAX Squad",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MutedTextLight
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.5.dp, MintAccent, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(-6.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(DarkCanvas)
                                    .border(1.5.dp, MintAccent, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "+${featuredTournament?.enrolledTeamsCount ?: 32} Teams Enrolled",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DarkCanvas
                                )
                            )
                        }

                        Button(
                            onClick = {
                                featuredTournament?.let { onSelectTournamentToPay(it) }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkCanvas,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("hero_join_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Pay & Enter", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Filter Row
        item {
            Column {
                Text(
                    text = "AVAILABLE TOURNAMENTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MutedTextLight
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        val isSelected = filter == selectedFilter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) MintAccent else SubCardBackground)
                                .border(
                                    1.dp,
                                    if (isSelected) MintBorder else CardBorder,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("filter_$filter")
                        ) {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = DarkCanvas
                                )
                            )
                        }
                    }
                }
            }
        }

        // Tournament List
        items(filteredTournaments) { tournament ->
            TournamentCardItem(
                tournament = tournament,
                onPayClick = { onSelectTournamentToPay(tournament) },
                onDetailClick = { onViewTournamentDetail(tournament) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TournamentCardItem(
    tournament: TournamentEntity,
    onPayClick: () -> Unit,
    onDetailClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tournament_card_${tournament.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(28.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SubCardBackground)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = DarkCanvas,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = tournament.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkCanvas
                            )
                        )
                        Text(
                            text = "${tournament.gameMode} • ${tournament.mapName} • ${tournament.startTimeFormatted}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MutedTextLight,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (tournament.status == "Live Now") Color(0xFFDC2626) else SubCardBackground)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tournament.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (tournament.status == "Live Now") Color.White else DarkCanvas,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SubCardBackground)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PRIZE POOL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedTextLight
                        )
                    )
                    Text(
                        text = tournament.prizePool,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkCanvas
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ENTRY FEE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedTextLight
                        )
                    )
                    Text(
                        text = "₹${tournament.entryFee}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkCanvas
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SLOTS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedTextLight
                        )
                    )
                    Text(
                        text = "${tournament.enrolledTeamsCount}/${tournament.maxTeams}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkCanvas
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDetailClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("detail_btn_${tournament.id}"),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Text("Rules & Room", fontSize = 12.sp, color = DarkCanvas, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onPayClick,
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("pay_btn_${tournament.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintAccent,
                        contentColor = DarkCanvas
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pay & Join", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}
