package com.najmizahrian.pokedex.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.najmizahrian.pokedex.data.model.PokemonDetail
import com.najmizahrian.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * State untuk layar Detail
 */
data class DetailUiState(
    val isLoading: Boolean = false,
    val pokemonDetail: PokemonDetail? = null,
    val errorMessage: String? = null
)

/**
 * ViewModel untuk mengelola state dan detail spesifik Pokémon
 */
class DetailViewModel(
    private val repository: PokemonRepository = PokemonRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    /**
     * Mengambil detail Pokémon berdasarkan ID atau nama
     */
    fun loadPokemonDetail(pokemonId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.getPokemonDetail(pokemonId)
            result.fold(
                onSuccess = { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pokemonDetail = detail,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Gagal memuat informasi Pokémon."
                        )
                    }
                }
            )
        }
    }
}
