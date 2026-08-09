package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.LeaderboardEntryEntity
import com.example.data.local.entity.TeamRegistrationEntity
import com.example.data.local.entity.TournamentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentDao {
    // Tournaments
    @Query("SELECT * FROM tournaments ORDER BY id DESC")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE id = :id")
    suspend fun getTournamentById(id: Long): TournamentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournament(tournament: TournamentEntity): Long

    @Query("UPDATE tournaments SET enrolledTeamsCount = enrolledTeamsCount + 1 WHERE id = :tournamentId")
    suspend fun incrementEnrolledCount(tournamentId: Long)

    @Query("UPDATE tournaments SET roomId = :roomId, roomPassword = :roomPassword, status = :status WHERE id = :tournamentId")
    suspend fun updateRoomDetails(tournamentId: Long, roomId: String, roomPassword: String, status: String)

    // Registrations
    @Query("SELECT * FROM team_registrations WHERE tournamentId = :tournamentId ORDER BY id DESC")
    fun getRegistrationsForTournament(tournamentId: Long): Flow<List<TeamRegistrationEntity>>

    @Query("SELECT * FROM team_registrations ORDER BY id DESC")
    fun getAllRegistrations(): Flow<List<TeamRegistrationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: TeamRegistrationEntity): Long

    @Query("UPDATE team_registrations SET paymentStatus = :status, aiVerificationNotes = :notes WHERE id = :registrationId")
    suspend fun updateRegistrationStatus(registrationId: Long, status: String, notes: String)

    // Leaderboard
    @Query("SELECT * FROM leaderboard_entries WHERE tournamentId = :tournamentId ORDER BY totalPoints DESC, kills DESC")
    fun getLeaderboardForTournament(tournamentId: Long): Flow<List<LeaderboardEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntry(entry: LeaderboardEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntries(entries: List<LeaderboardEntryEntity>)

    @Query("DELETE FROM leaderboard_entries WHERE tournamentId = :tournamentId")
    suspend fun clearLeaderboardForTournament(tournamentId: Long)

    @Query("SELECT COUNT(*) FROM tournaments")
    suspend fun getTournamentCount(): Int
}
