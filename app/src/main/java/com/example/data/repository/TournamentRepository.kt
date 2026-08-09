package com.example.data.repository

import com.example.data.local.dao.TournamentDao
import com.example.data.local.entity.LeaderboardEntryEntity
import com.example.data.local.entity.TeamRegistrationEntity
import com.example.data.local.entity.TournamentEntity
import kotlinx.coroutines.flow.Flow

class TournamentRepository(private val dao: TournamentDao) {

    val allTournaments: Flow<List<TournamentEntity>> = dao.getAllTournaments()
    val allRegistrations: Flow<List<TeamRegistrationEntity>> = dao.getAllRegistrations()

    fun getLeaderboard(tournamentId: Long): Flow<List<LeaderboardEntryEntity>> {
        return dao.getLeaderboardForTournament(tournamentId)
    }

    fun getRegistrations(tournamentId: Long): Flow<List<TeamRegistrationEntity>> {
        return dao.getRegistrationsForTournament(tournamentId)
    }

    suspend fun registerTeam(
        tournamentId: Long,
        teamName: String,
        uid: String,
        upiName: String,
        utrNumber: String,
        status: String,
        aiNotes: String
    ): Long {
        val registration = TeamRegistrationEntity(
            tournamentId = tournamentId,
            teamName = teamName,
            leaderInGameUid = uid,
            leaderUpiName = upiName,
            utrNumber = utrNumber,
            paymentStatus = status,
            aiVerificationNotes = aiNotes
        )
        val regId = dao.insertRegistration(registration)
        if (status == "VERIFIED") {
            dao.incrementEnrolledCount(tournamentId)
            // Add initial entry into leaderboard
            dao.insertLeaderboardEntry(
                LeaderboardEntryEntity(
                    tournamentId = tournamentId,
                    teamName = teamName,
                    kills = 0,
                    booyahs = 0,
                    placementPoints = 10,
                    totalPoints = 10
                )
            )
        }
        return regId
    }

    suspend fun createTournament(tournament: TournamentEntity): Long {
        return dao.insertTournament(tournament)
    }

    suspend fun updateRoomDetails(tournamentId: Long, roomId: String, roomPass: String, status: String) {
        dao.updateRoomDetails(tournamentId, roomId, roomPass, status)
    }

    suspend fun updateRegistrationStatus(regId: Long, status: String, notes: String) {
        dao.updateRegistrationStatus(regId, status, notes)
    }

    suspend fun updateLeaderboard(entries: List<LeaderboardEntryEntity>) {
        if (entries.isNotEmpty()) {
            dao.insertLeaderboardEntries(entries)
        }
    }

    suspend fun seedInitialDataIfNeeded() {
        if (dao.getTournamentCount() == 0) {
            val t1Id = dao.insertTournament(
                TournamentEntity(
                    title = "Squad Champions League",
                    gameMode = "Squad",
                    mapName = "Bermuda",
                    prizePool = "₹50,000",
                    entryFee = 500,
                    maxTeams = 48,
                    enrolledTeamsCount = 32,
                    status = "Registration Open",
                    roomId = "FFMAX-8841",
                    roomPassword = "991",
                    startTimeFormatted = "Today, 08:00 PM IST",
                    rules = "Squad (4 Person) • Kill = 2 Points | Booyah! = 12 Points. Organised by Sangam."
                )
            )

            val t2Id = dao.insertTournament(
                TournamentEntity(
                    title = "Duo Group Showdown",
                    gameMode = "Duo",
                    mapName = "Kalahari",
                    prizePool = "₹20,000",
                    entryFee = 200,
                    maxTeams = 24,
                    enrolledTeamsCount = 24,
                    status = "Live Now",
                    roomId = "FFMAX-7023",
                    roomPassword = "442",
                    startTimeFormatted = "LIVE NOW",
                    rules = "Duo Group • Kill = 2 Points | Booyah! = 12 Points. Organised by Sangam."
                )
            )

            val t3Id = dao.insertTournament(
                TournamentEntity(
                    title = "Single Deathmatch Solo",
                    gameMode = "Solo",
                    mapName = "Purgatory",
                    prizePool = "₹15,000",
                    entryFee = 150,
                    maxTeams = 48,
                    enrolledTeamsCount = 18,
                    status = "Registration Open",
                    roomId = "",
                    roomPassword = "",
                    startTimeFormatted = "Tomorrow, 06:00 PM IST",
                    rules = "Single / Solo • Highest kills take the Gold Trophy. Organised by Sangam."
                )
            )

            // Seed initial leaderboard for Pro Circuit S4 (t1Id)
            val initialLeaderboard = listOf(
                LeaderboardEntryEntity(
                    tournamentId = t1Id,
                    teamName = "Team_Viper",
                    kills = 22,
                    booyahs = 1,
                    placementPoints = 98,
                    totalPoints = 142,
                    rankPosition = 1
                ),
                LeaderboardEntryEntity(
                    tournamentId = t1Id,
                    teamName = "ShadowKings",
                    kills = 18,
                    booyahs = 0,
                    placementPoints = 62,
                    totalPoints = 98,
                    rankPosition = 2
                ),
                LeaderboardEntryEntity(
                    tournamentId = t1Id,
                    teamName = "Executioners",
                    kills = 15,
                    booyahs = 1,
                    placementPoints = 64,
                    totalPoints = 94,
                    rankPosition = 3
                ),
                LeaderboardEntryEntity(
                    tournamentId = t1Id,
                    teamName = "Team_Hydra",
                    kills = 12,
                    booyahs = 0,
                    placementPoints = 52,
                    totalPoints = 76,
                    rankPosition = 4
                ),
                LeaderboardEntryEntity(
                    tournamentId = t1Id,
                    teamName = "AlphaSquad",
                    kills = 10,
                    booyahs = 0,
                    placementPoints = 42,
                    totalPoints = 62,
                    rankPosition = 5
                ),
                LeaderboardEntryEntity(
                    tournamentId = t1Id,
                    teamName = "RedDevils",
                    kills = 8,
                    booyahs = 0,
                    placementPoints = 32,
                    totalPoints = 48,
                    rankPosition = 6
                )
            )
            dao.insertLeaderboardEntries(initialLeaderboard)

            // Seed initial verified registration
            dao.insertRegistration(
                TeamRegistrationEntity(
                    tournamentId = t1Id,
                    teamName = "Team_Viper",
                    leaderInGameUid = "84291039",
                    leaderUpiName = "Sangam Nishad",
                    utrNumber = "428910398201",
                    paymentStatus = "VERIFIED",
                    aiVerificationNotes = "AI verified 100% matched payment for sangamnishad@fam"
                )
            )
        }
    }
}
