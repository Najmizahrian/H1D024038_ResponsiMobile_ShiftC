package com.najmizahrian.pokedex.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data model DTO untuk response detail Pokémon dari PokéAPI
 * Endpoint: /api/v2/pokemon/{id atau name}
 */
data class PokemonDetailResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("height")
    val height: Int, // Dalam decimetres (1 dm = 0.1 m)

    @SerializedName("weight")
    val weight: Int, // Dalam hectograms (1 hg = 0.1 kg)

    @SerializedName("base_experience")
    val baseExperience: Int?,

    @SerializedName("types")
    val types: List<PokemonTypeSlot>,

    @SerializedName("stats")
    val stats: List<PokemonStatSlot>,

    @SerializedName("abilities")
    val abilities: List<PokemonAbilitySlot>?,

    @SerializedName("sprites")
    val sprites: PokemonSprites?
)

data class PokemonTypeSlot(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: NamedResource
)

data class PokemonStatSlot(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("stat")
    val stat: NamedResource
)

data class PokemonAbilitySlot(
    @SerializedName("ability")
    val ability: NamedResource,
    @SerializedName("is_hidden")
    val isHidden: Boolean,
    @SerializedName("slot")
    val slot: Int
)

data class NamedResource(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class PokemonSprites(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("other")
    val other: OtherSprites?
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String?
)
