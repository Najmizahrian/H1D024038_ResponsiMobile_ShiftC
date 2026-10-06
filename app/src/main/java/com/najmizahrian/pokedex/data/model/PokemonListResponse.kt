package com.najmizahrian.pokedex.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data model DTO untuk response daftar Pokémon dari PokéAPI
 * Endpoint: /api/v2/pokemon?limit=151
 */
data class PokemonListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<PokemonNamedResource>
)

data class PokemonNamedResource(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) {
    /**
     * Mengekstrak ID Pokémon dari URL resource
     * Contoh: https://pokeapi.co/api/v2/pokemon/25/ -> 25
     */
    val id: Int
        get() {
            val segments = url.trimEnd('/').split('/')
            return segments.lastOrNull()?.toIntOrNull() ?: 0
        }

    /**
     * URL official artwork Pokémon dengan resolusi tinggi
     */
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}
