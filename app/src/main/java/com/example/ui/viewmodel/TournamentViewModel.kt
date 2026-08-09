package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.LeaderboardEntryEntity
import com.example.data.local.entity.TeamRegistrationEntity
import com.example.data.local.entity.TournamentEntity
import com.example.data.remote.AiVerificationResult
import com.example.data.remote.GeminiPaymentVerifier
import com.example.data.repository.TournamentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class TournamentViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = TournamentRepository(db.tournamentDao())

    val tournaments: StateFlow<List<TournamentEntity>> = repository.allTournaments
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allRegistrations: StateFlow<List<TeamRegistrationEntity>> = repository.allRegistrations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedTournamentId = MutableStateFlow<Long>(1L)
    val selectedTournamentId: StateFlow<Long> = _selectedTournamentId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val leaderboard: StateFlow<List<LeaderboardEntryEntity>> = _selectedTournamentId
        .flatMapLatest { id -> repository.getLeaderboard(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isVerifyingPayment = MutableStateFlow(false)
    val isVerifyingPayment: StateFlow<Boolean> = _isVerifyingPayment.asStateFlow()

    private val _verificationResult = MutableStateFlow<AiVerificationResult?>(null)
    val verificationResult: StateFlow<AiVerificationResult?> = _verificationResult.asStateFlow()

    private val _liveUpdateFlash = MutableStateFlow(false)
    val liveUpdateFlash: StateFlow<Boolean> = _liveUpdateFlash.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun selectTournament(id: Long) {
        _selectedTournamentId.value = id
    }

    fun clearVerificationState() {
        _verificationResult.value = null
    }

    fun submitPaymentForVerification(
        tournament: TournamentEntity,
        teamName: String,
        uid: String,
        upiName: String,
        utrNumber: String,
        screenshotBitmap: Bitmap? = null
    ) {
        viewModelScope.launch {
            _isVerifyingPayment.value = true
            _verificationResult.value = null

            val result = GeminiPaymentVerifier.verifyPaymentWithAi(
                utrInput = utrNumber,
                teamName = teamName,
                uid = uid,
                tournamentTitle = tournament.title,
                entryFeeAmount = tournament.entryFee,
                screenshotBitmap = screenshotBitmap
            )

            _verificationResult.value = result
            _isVerifyingPayment.value = false

            if (result.isVerified) {
                repository.registerTeam(
                    tournamentId = tournament.id,
                    teamName = teamName,
                    uid = uid,
                    upiName = upiName,
                    utrNumber = utrNumber,
                    status = "VERIFIED",
                    aiNotes = "${result.statusLabel}: ${result.summaryNotes}"
                )
            } else {
                repository.registerTeam(
                    tournamentId = tournament.id,
                    teamName = teamName,
                    uid = uid,
                    upiName = upiName,
                    utrNumber = utrNumber,
                    status = "PENDING",
                    aiNotes = "${result.statusLabel}: ${result.summaryNotes}"
                )
            }
        }
    }

    fun simulateLiveMatchEvent() {
        viewModelScope.launch {
            val currentList = leaderboard.value
            if (currentList.isEmpty()) return@launch

            val indexToUpdate = Random.nextInt(currentList.size)
            val target = currentList[indexToUpdate]
            val extraKills = Random.nextInt(1, 4)
            val updatedKills = target.kills + extraKills
            val extraPoints = extraKills * 2
            val updatedTotal = target.totalPoints + extraPoints

            val updatedList = currentList.toMutableList()
            updatedList[indexToUpdate] = target.copy(
                kills = updatedKills,
                totalPoints = updatedTotal
            )

            // Re-sort and assign ranks
            val sorted = updatedList.sortedByDescending { it.totalPoints }
                .mapIndexed { idx, entry -> entry.copy(rankPosition = idx + 1) }

            repository.updateLeaderboard(sorted)

            _liveUpdateFlash.value = true
            delay(1200)
            _liveUpdateFlash.value = false
        }
    }

    fun createNewTournament(
        title: String,
        gameMode: String,
        mapName: String,
        prizePool: String,
        entryFee: Int,
        startTime: String
    ) {
        viewModelScope.launch {
            val newTourney = TournamentEntity(
                title = title,
                gameMode = gameMode,
                mapName = mapName,
                prizePool = prizePool,
                entryFee = entryFee,
                status = "Registration Open",
                startTimeFormatted = startTime
            )
            repository.createTournament(newTourney)
        }
    }

    fun publishRoomDetails(tournamentId: Long, roomId: String, roomPass: String) {
        viewModelScope.launch {
            repository.updateRoomDetails(tournamentId, roomId, roomPass, "Live Now")
        }
    }

    fun updateRegistrationStatusManually(registrationId: Long, status: String, notes: String) {
        viewModelScope.launch {
            repository.updateRegistrationStatus(registrationId, status, notes)
        }
    }
}
