package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entity.TournamentEntity
import com.example.ui.screens.EventsScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.OrganiserScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MutedTextLight
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.TournamentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainTournamentApp()
            }
        }
    }
}

enum class NavigationTab(val title: String) {
    EVENTS("Tournaments"),
    PAYMENT("Pay & QR"),
    LEADERBOARD("Leaderboard"),
    ORGANISER("Organiser")
}

@Composable
fun MainTournamentApp(viewModel: TournamentViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(NavigationTab.EVENTS) }

    val tournaments by viewModel.tournaments.collectAsState()
    val leaderboard by viewModel.leaderboard.collectAsState()
    val registrations by viewModel.allRegistrations.collectAsState()
    val selectedTournamentId by viewModel.selectedTournamentId.collectAsState()
    val isVerifying by viewModel.isVerifyingPayment.collectAsState()
    val verificationResult by viewModel.verificationResult.collectAsState()
    val liveUpdateFlash by viewModel.liveUpdateFlash.collectAsState()

    var selectedTournamentForPay by remember { mutableStateOf<TournamentEntity?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = DarkCanvas,
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = selectedTab == NavigationTab.EVENTS,
                    onClick = { selectedTab = NavigationTab.EVENTS },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Tournaments"
                        )
                    },
                    label = {
                        Text(
                            text = NavigationTab.EVENTS.title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == NavigationTab.EVENTS) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkCanvas,
                        selectedTextColor = MintAccent,
                        indicatorColor = MintAccent,
                        unselectedIconColor = MutedTextLight,
                        unselectedTextColor = MutedTextLight
                    ),
                    modifier = Modifier.testTag("tab_events")
                )

                NavigationBarItem(
                    selected = selectedTab == NavigationTab.PAYMENT,
                    onClick = { selectedTab = NavigationTab.PAYMENT },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Pay & QR"
                        )
                    },
                    label = {
                        Text(
                            text = NavigationTab.PAYMENT.title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == NavigationTab.PAYMENT) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkCanvas,
                        selectedTextColor = MintAccent,
                        indicatorColor = MintAccent,
                        unselectedIconColor = MutedTextLight,
                        unselectedTextColor = MutedTextLight
                    ),
                    modifier = Modifier.testTag("tab_payment")
                )

                NavigationBarItem(
                    selected = selectedTab == NavigationTab.LEADERBOARD,
                    onClick = { selectedTab = NavigationTab.LEADERBOARD },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Leaderboard,
                            contentDescription = "Leaderboard"
                        )
                    },
                    label = {
                        Text(
                            text = NavigationTab.LEADERBOARD.title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == NavigationTab.LEADERBOARD) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkCanvas,
                        selectedTextColor = MintAccent,
                        indicatorColor = MintAccent,
                        unselectedIconColor = MutedTextLight,
                        unselectedTextColor = MutedTextLight
                    ),
                    modifier = Modifier.testTag("tab_leaderboard")
                )

                NavigationBarItem(
                    selected = selectedTab == NavigationTab.ORGANISER,
                    onClick = { selectedTab = NavigationTab.ORGANISER },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Organiser"
                        )
                    },
                    label = {
                        Text(
                            text = NavigationTab.ORGANISER.title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == NavigationTab.ORGANISER) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkCanvas,
                        selectedTextColor = MintAccent,
                        indicatorColor = MintAccent,
                        unselectedIconColor = MutedTextLight,
                        unselectedTextColor = MutedTextLight
                    ),
                    modifier = Modifier.testTag("tab_organiser")
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            NavigationTab.EVENTS -> {
                EventsScreen(
                    tournaments = tournaments,
                    onSelectTournamentToPay = { tourney ->
                        selectedTournamentForPay = tourney
                        selectedTab = NavigationTab.PAYMENT
                    },
                    onViewTournamentDetail = { tourney ->
                        viewModel.selectTournament(tourney.id)
                        selectedTab = NavigationTab.LEADERBOARD
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            NavigationTab.PAYMENT -> {
                PaymentScreen(
                    tournaments = tournaments,
                    selectedTournament = selectedTournamentForPay ?: tournaments.firstOrNull(),
                    isVerifying = isVerifying,
                    verificationResult = verificationResult,
                    onVerifyPayment = { tourney, teamName, uid, upiName, utr, bmp ->
                        viewModel.submitPaymentForVerification(tourney, teamName, uid, upiName, utr, bmp)
                    },
                    onClearResult = { viewModel.clearVerificationState() },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            NavigationTab.LEADERBOARD -> {
                LeaderboardScreen(
                    tournaments = tournaments,
                    selectedTournamentId = selectedTournamentId,
                    leaderboardEntries = leaderboard,
                    isLiveFlashing = liveUpdateFlash,
                    onSelectTournament = { id -> viewModel.selectTournament(id) },
                    onSimulateKill = { viewModel.simulateLiveMatchEvent() },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            NavigationTab.ORGANISER -> {
                OrganiserScreen(
                    tournaments = tournaments,
                    registrations = registrations,
                    onCreateTournament = { title, mode, map, prize, fee, start ->
                        viewModel.createNewTournament(title, mode, map, prize, fee, start)
                    },
                    onPublishRoom = { tourneyId, roomId, roomPass ->
                        viewModel.publishRoomDetails(tourneyId, roomId, roomPass)
                    },
                    onApproveRegistration = { regId, status, notes ->
                        viewModel.updateRegistrationStatusManually(regId, status, notes)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
