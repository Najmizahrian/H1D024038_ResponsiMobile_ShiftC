package com.najmizahrian.pokedex.ui.screens.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.najmizahrian.pokedex.data.model.PokemonDetail
import com.najmizahrian.pokedex.data.model.PokemonItem
import com.najmizahrian.pokedex.data.model.TypeChartHelper
import com.najmizahrian.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Ringkasan statistik gabungan tim Pokémon (Party of 6)
 */
data class TeamStatsSummary(
    val totalHp: Int = 0,
    val totalAttack: Int = 0,
    val totalDefense: Int = 0,
    val totalSpAtk: Int = 0,
    val totalSpDef: Int = 0,
    val totalSpeed: Int = 0,
    val avgHp: Int = 0,
    val avgAttack: Int = 0,
    val avgDefense: Int = 0,
    val avgSpAtk: Int = 0,
    val avgSpDef: Int = 0,
    val avgSpeed: Int = 0,
    val totalBaseStats: Int = 0
)

/**
 * State UI untuk layar PokéTeam Builder & Synergy Roster
 */
data class TeamUiState(
    val slots: List<PokemonDetail?> = List(6) { null },
    val availablePokemonList: List<PokemonItem> = emptyList(),
    val filteredAvailableList: List<PokemonItem> = emptyList(),
    val selectorQuery: String = "",
    val activeSelectorSlotIndex: Int? = null,
    val isLoading: Boolean = false,
    val statsSummary: TeamStatsSummary = TeamStatsSummary(),
    val offensiveCoverage: List<String> = emptyList(),
    val weaknesses: Map<String, Int> = emptyMap(),
    val synergyScore: Int = 0,
    val synergyRank: String = "-",
    val notificationMessage: String? = null
)

/**
 * ViewModel untuk mengelola logika pembentukan tim dan analisis sinergi elemen
 */
class TeamViewModel(
    private val repository: PokemonRepository = PokemonRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamUiState())
    val uiState: StateFlow<TeamUiState> = _uiState.asStateFlow()

    init {
        loadAvailablePokemonCatalog()
        // Muat preset awal agar tampilan langsung menarik saat pertama dibuka
        loadInitialPreset()
    }

    /**
     * Memuat katalog Pokémon untuk dialog pemilihan slot
     */
    fun loadAvailablePokemonCatalog() {
        viewModelScope.launch {
            repository.getPokemonList()
                .onSuccess { list ->
                    _uiState.update { current ->
                        current.copy(
                            availablePokemonList = list,
                            filteredAvailableList = list
                        )
                    }
                }
        }
    }

    /**
     * Memuat tim awal (Charizard, Blastoise, Venusaur) sebagai contoh
     */
    private fun loadInitialPreset() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val initialIds = listOf(6, 9, 3) // Charizard, Blastoise, Venusaur
            val initialSlots = MutableList<PokemonDetail?>(6) { null }

            for ((index, id) in initialIds.withIndex()) {
                repository.getPokemonDetail(id.toString()).onSuccess { detail ->
                    initialSlots[index] = detail
                }
            }

            _uiState.update { it.copy(slots = initialSlots, isLoading = false) }
            recalculateTeamSynergy()
        }
    }

    /**
     * Buka modal pemilih Pokémon untuk slot tertentu (0 - 5)
     */
    fun openSelector(slotIndex: Int) {
        _uiState.update {
            it.copy(
                activeSelectorSlotIndex = slotIndex,
                selectorQuery = "",
                filteredAvailableList = it.availablePokemonList
            )
        }
    }

    /**
     * Tutup modal pemilih Pokémon
     */
    fun closeSelector() {
        _uiState.update { it.copy(activeSelectorSlotIndex = null) }
    }

    /**
     * Filter pencarian Pokémon pada modal pemilih
     */
    fun onSelectorQueryChange(query: String) {
        _uiState.update { current ->
            val filtered = if (query.isBlank()) {
                current.availablePokemonList
            } else {
                current.availablePokemonList.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.id.toString() == query.trim() ||
                            it.primaryType.contains(query, ignoreCase = true)
                }
            }
            current.copy(selectorQuery = query, filteredAvailableList = filtered)
        }
    }

    /**
     * Memilih Pokémon ke dalam slot yang aktif
     */
    fun selectPokemonForSlot(slotIndex: Int, pokemonId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, activeSelectorSlotIndex = null) }
            repository.getPokemonDetail(pokemonId.toString())
                .onSuccess { detail ->
                    val newSlots = _uiState.value.slots.toMutableList()
                    newSlots[slotIndex] = detail
                    _uiState.update {
                        it.copy(
                            slots = newSlots,
                            isLoading = false,
                            notificationMessage = "${detail.displayName} berhasil dimasukkan ke Slot #${slotIndex + 1}!"
                        )
                    }
                    recalculateTeamSynergy()
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notificationMessage = "Gagal memuat Pokémon terpilih."
                        )
                    }
                }
        }
    }

    /**
     * Menghapus Pokémon dari slot tertentu
     */
    fun removePokemonFromSlot(slotIndex: Int) {
        val newSlots = _uiState.value.slots.toMutableList()
        val removedName = newSlots[slotIndex]?.displayName ?: "Pokémon"
        newSlots[slotIndex] = null
        _uiState.update {
            it.copy(
                slots = newSlots,
                notificationMessage = "$removedName dihapus dari tim."
            )
        }
        recalculateTeamSynergy()
    }

    /**
     * Mengosongkan seluruh slot tim
     */
    fun clearTeam() {
        _uiState.update {
            it.copy(
                slots = List(6) { null },
                notificationMessage = "Seluruh slot tim telah dikosongkan."
            )
        }
        recalculateTeamSynergy()
    }

    /**
     * Memuat Preset Tim Juara Kanto (Charizard, Blastoise, Venusaur, Pikachu, Gengar, Snorlax)
     */
    fun fillKantoChampionsPreset() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val presetIds = listOf(6, 9, 3, 25, 94, 143)
            val newSlots = MutableList<PokemonDetail?>(6) { null }

            for ((index, id) in presetIds.withIndex()) {
                repository.getPokemonDetail(id.toString()).onSuccess { detail ->
                    newSlots[index] = detail
                }
            }

            _uiState.update {
                it.copy(
                    slots = newSlots,
                    isLoading = false,
                    notificationMessage = "Preset Tim Juara Kanto (6 Pokémon) berhasil dimuat!"
                )
            }
            recalculateTeamSynergy()
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }

    /**
     * Menghitung ulang seluruh analitik tim: statistik, cakupan ofensif, dan kelemahan tipe
     */
    private fun recalculateTeamSynergy() {
        val currentSlots = _uiState.value.slots
        val activeMembers = currentSlots.filterNotNull()
        val count = activeMembers.size

        if (count == 0) {
            _uiState.update {
                it.copy(
                    statsSummary = TeamStatsSummary(),
                    offensiveCoverage = emptyList(),
                    weaknesses = emptyMap(),
                    synergyScore = 0,
                    synergyRank = "-"
                )
            }
            return
        }

        // Hitung statistik
        var sumHp = 0
        var sumAtk = 0
        var sumDef = 0
        var sumSpAtk = 0
        var sumSpDef = 0
        var sumSpeed = 0

        for (member in activeMembers) {
            for (stat in member.stats) {
                when (stat.name.lowercase()) {
                    "hp" -> sumHp += stat.value
                    "attack" -> sumAtk += stat.value
                    "defense" -> sumDef += stat.value
                    "special-attack" -> sumSpAtk += stat.value
                    "special-defense" -> sumSpDef += stat.value
                    "speed" -> sumSpeed += stat.value
                }
            }
        }

        val totalBase = sumHp + sumAtk + sumDef + sumSpAtk + sumSpDef + sumSpeed
        val statsSummary = TeamStatsSummary(
            totalHp = sumHp,
            totalAttack = sumAtk,
            totalDefense = sumDef,
            totalSpAtk = sumSpAtk,
            totalSpDef = sumSpDef,
            totalSpeed = sumSpeed,
            avgHp = sumHp / count,
            avgAttack = sumAtk / count,
            avgDefense = sumDef / count,
            avgSpAtk = sumSpAtk / count,
            avgSpDef = sumSpDef / count,
            avgSpeed = sumSpeed / count,
            totalBaseStats = totalBase
        )

        // Hitung cakupan tipe ofensif dan defensif
        val teamTypes = activeMembers.map { it.types }
        val coverage = TypeChartHelper.calculateTeamCoverage(teamTypes)
        val weaknesses = TypeChartHelper.calculateTeamWeaknessFrequency(teamTypes)

        val totalWeaknessCount = weaknesses.values.sum()
        val (score, rank) = TypeChartHelper.calculateSynergyScore(
            memberCount = count,
            coveredTypesCount = coverage.size,
            totalWeaknessCount = totalWeaknessCount
        )

        _uiState.update {
            it.copy(
                statsSummary = statsSummary,
                offensiveCoverage = coverage,
                weaknesses = weaknesses,
                synergyScore = score,
                synergyRank = rank
            )
        }
    }
}
