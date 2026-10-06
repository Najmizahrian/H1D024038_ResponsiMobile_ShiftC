package com.najmizahrian.pokedex.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.najmizahrian.pokedex.data.model.PokemonItem
import com.najmizahrian.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * State untuk layar Home
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val pokemonList: List<PokemonItem> = emptyList(),
    val filteredList: List<PokemonItem> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null
)

/**
 * ViewModel untuk mengelola state dan data di HomeScreen
 */
class HomeViewModel(
    private val repository: PokemonRepository = PokemonRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPokemonList()
    }

    /**
     * Memuat daftar Pokémon dari repository
     */
    fun loadPokemonList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.getPokemonList()
            result.fold(
                onSuccess = { list ->
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            pokemonList = list,
                            filteredList = filterList(list, current.searchQuery),
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Gagal memuat data Pokémon. Periksa koneksi internet Anda."
                        )
                    }
                }
            )
        }
    }

    /**
     * Mengubah query pencarian dan memfilter Pokémon secara real-time
     */
    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { current ->
            current.copy(
                searchQuery = newQuery,
                filteredList = filterList(current.pokemonList, newQuery)
            )
        }
    }

    /**
     * Menghapus query pencarian
     */
    fun clearSearchQuery() {
        onSearchQueryChange("")
    }

    private fun filterList(list: List<PokemonItem>, query: String): List<PokemonItem> {
        val trimmed = query.trim().lowercase()
        return if (trimmed.isEmpty()) {
            list
        } else {
            list.filter {
                it.name.lowercase().contains(trimmed) ||
                        it.id.toString() == trimmed ||
                        it.formattedId.lowercase().contains(trimmed)
            }
        }
    }
}
