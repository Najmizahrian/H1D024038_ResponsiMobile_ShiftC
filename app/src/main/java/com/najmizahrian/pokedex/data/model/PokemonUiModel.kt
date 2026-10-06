package com.najmizahrian.pokedex.data.model

/**
 * Model UI untuk item Pokémon yang ditampilkan pada list / grid
 */
data class PokemonItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val primaryType: String = PokemonTypeHelper.getTypeForId(id)
) {
    val formattedId: String
        get() = "#%03d".format(id)

    val displayName: String
        get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

/**
 * Model UI untuk detail lengkap Pokémon
 */
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val heightInMeters: Double,
    val weightInKg: Double,
    val baseExperience: Int?,
    val types: List<String>,
    val stats: List<PokemonStatItem>,
    val abilities: List<String>
) {
    val formattedId: String
        get() = "#%03d".format(id)

    val displayName: String
        get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    val primaryType: String
        get() = types.firstOrNull() ?: "normal"
}

data class PokemonStatItem(
    val name: String,
    val value: Int,
    val maxStatValue: Int = 200
) {
    val percentage: Float
        get() = (value.toFloat() / maxStatValue.toFloat()).coerceIn(0f, 1f)

    val displayLabel: String
        get() = when (name.lowercase()) {
            "hp" -> "HP"
            "attack" -> "Attack"
            "defense" -> "Defense"
            "special-attack" -> "Sp. Atk"
            "special-defense" -> "Sp. Def"
            "speed" -> "Speed"
            else -> name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}
