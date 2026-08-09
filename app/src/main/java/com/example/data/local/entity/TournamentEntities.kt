package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val gameMode: String, // "Squad", "Duo", "Solo"
    val mapName: String, // "Bermuda", "Purgatory", "Kalahari"
    val prizePool: String, // e.g. "₹50,000" or "$2,500"
    val entryFee: Int, // e.g. 50 (in ₹)
    val maxTeams: Int = 48,
    val enrolledTeamsCount: Int = 0,
    val status: String, // "Registration Open", "Live Now", "Completed"
    val roomId: String = "",
    val roomPassword: String = "",
    val startTimeFormatted: String,
    val rules: String = "1 Kill = 2 Points. Booyah = 12 Points. No Emote Banning, Emulator Blocked."
)

@Entity(tableName = "team_registrations")
data class TeamRegistrationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tournamentId: Long,
    val teamName: String,
    val leaderInGameUid: String,
    val leaderUpiName: String,
    val utrNumber: String,
    val paymentStatus: String, // "VERIFIED", "PENDING", "REJECTED"
    val aiVerificationNotes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "leaderboard_entries")
data class LeaderboardEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tournamentId: Long,
    val teamName: String,
    val kills: Int,
    val booyahs: Int,
    val placementPoints: Int,
    val totalPoints: Int,
    val matchNumber: Int = 1,
    val rankPosition: Int = 0
)
