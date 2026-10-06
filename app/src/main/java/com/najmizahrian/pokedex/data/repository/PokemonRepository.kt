package com.najmizahrian.pokedex.data.repository

import com.najmizahrian.pokedex.data.model.PokemonDetail
import com.najmizahrian.pokedex.data.model.PokemonItem
import com.najmizahrian.pokedex.data.model.PokemonStatItem
import com.najmizahrian.pokedex.data.remote.PokeApiService
import com.najmizahrian.pokedex.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository yang mengabstraksi sumber data (PokéAPI)
 * dan mentransformasikannya ke UI Model
 */
class PokemonRepository(
    private val apiService: PokeApiService = RetrofitClient.apiService
) {

    /**
     * Mengambil daftar Pokémon dari PokéAPI
     */
    suspend fun getPokemonList(): Result<List<PokemonItem>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPokemonList(limit = 151, offset = 0)
            val pokemonList = response.results.map { resource ->
                PokemonItem(
                    id = resource.id,
                    name = resource.name,
                    imageUrl = resource.imageUrl
                )
            }
            Result.success(pokemonList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mengambil detail lengkap Pokémon berdasarkan ID atau nama
     */
    suspend fun getPokemonDetail(nameOrId: String): Result<PokemonDetail> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPokemonDetail(nameOrId.lowercase())

            // Ambil URL gambar official artwork atau fallback ke front_default
            val officialArtwork = response.sprites?.other?.officialArtwork?.frontDefault
            val frontDefault = response.sprites?.frontDefault
            val imageUrl = officialArtwork
                ?: frontDefault
                ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${response.id}.png"

            // Konversi tinggi (decimeter -> meter) dan berat (hectogram -> kg)
            val heightInMeters = response.height / 10.0
            val weightInKg = response.weight / 10.0

            // Pemetaan tipe Pokémon
            val types = response.types.map { it.type.name }

            // Pemetaan statistik dasar Pokémon
            val stats = response.stats.map { statSlot ->
                PokemonStatItem(
                    name = statSlot.stat.name,
                    value = statSlot.baseStat
                )
            }

            // Pemetaan abilities
            val abilities = response.abilities?.map { it.ability.name } ?: emptyList()

            val detail = PokemonDetail(
                id = response.id,
                name = response.name,
                imageUrl = imageUrl,
                heightInMeters = heightInMeters,
                weightInKg = weightInKg,
                baseExperience = response.baseExperience,
                types = types,
                stats = stats,
                abilities = abilities
            )

            Result.success(detail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
