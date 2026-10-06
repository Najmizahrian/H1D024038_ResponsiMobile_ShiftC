package com.najmizahrian.pokedex.ui.theme

import androidx.compose.ui.graphics.Color

val CobaltBlue = Color(0xFF1976D2)
val DeepCobalt = Color(0xFF0D47A1)
val ElectricCyan = Color(0xFF00E5FF)
val NavyDark = Color(0xFF0A192F)
val NavySurface = Color(0xFF112240)
val IceBlue = Color(0xFFF0F4F8)

val PrimaryBlue80 = Color(0xFF90CAF9)
val SecondaryCyan80 = Color(0xFF80DEEA)
val TertiaryTeal80 = Color(0xFFA7FFEB)

val PrimaryBlue40 = Color(0xFF1565C0)
val SecondaryCyan40 = Color(0xFF00838F)
val TertiaryTeal40 = Color(0xFF00695C)

// Pokémon Element Type Colors
val TypeNormal = Color(0xFFA8A878)
val TypeFire = Color(0xFFFB6C6C)
val TypeWater = Color(0xFF77BDFE)
val TypeGrass = Color(0xFF48D0B0)
val TypeElectric = Color(0xFFFFCE4B)
val TypeIce = Color(0xFF81D4FA)
val TypeFighting = Color(0xFFD35400)
val TypePoison = Color(0xFFA569BD)
val TypeGround = Color(0xFFE0C068)
val TypeFlying = Color(0xFFA890F0)
val TypePsychic = Color(0xFFFF76AC)
val TypeBug = Color(0xFFA8B820)
val TypeRock = Color(0xFFC5B064)
val TypeGhost = Color(0xFF7E69B5)
val TypeDragon = Color(0xFF7C5CDA)
val TypeSteel = Color(0xFF9EA3A8)
val TypeFairy = Color(0xFFF4A6C7)
val TypeDark = Color(0xFF705848)

// Stat Progress Bar Colors
val StatHpColor = Color(0xFFFF5959)
val StatAttackColor = Color(0xFFF5AC78)
val StatDefenseColor = Color(0xFFFAE078)
val StatSpAtkColor = Color(0xFF9DB7F5)
val StatSpDefColor = Color(0xFFA7DB8D)
val StatSpeedColor = Color(0xFFFA92B2)

/**
 * Mendapatkan warna khas berdasarkan nama tipe Pokémon
 */
fun getPokemonTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "grass" -> TypeGrass
        "electric" -> TypeElectric
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        "dark" -> TypeDark
        else -> Color(0xFF888888)
    }
}

/**
 * Mendapatkan gradien modern untuk latar belakang kartu Pokémon
 */
fun getPokemonGradient(type: String): List<Color> {
    return when (type.lowercase()) {
        "grass" -> listOf(Color(0xFF48D0B0), Color(0xFF2CB896))
        "fire" -> listOf(Color(0xFFFB6C6C), Color(0xFFE24F4F))
        "water" -> listOf(Color(0xFF77BDFE), Color(0xFF479AF8))
        "electric" -> listOf(Color(0xFFFFCE4B), Color(0xFFE5A71E))
        "poison" -> listOf(Color(0xFFA569BD), Color(0xFF8E44AD))
        "bug" -> listOf(Color(0xFFA8B820), Color(0xFF8D9C14))
        "normal" -> listOf(Color(0xFFB8B8A8), Color(0xFF9E9E8C))
        "ground" -> listOf(Color(0xFFE0C068), Color(0xFFC7A246))
        "fairy" -> listOf(Color(0xFFF4A6C7), Color(0xFFE57FA7))
        "fighting" -> listOf(Color(0xFFD35400), Color(0xFFB94400))
        "psychic" -> listOf(Color(0xFFFF76AC), Color(0xFFE84F8C))
        "rock" -> listOf(Color(0xFFC5B064), Color(0xFFA89446))
        "ghost" -> listOf(Color(0xFF7E69B5), Color(0xFF654E9E))
        "ice" -> listOf(Color(0xFF81D4FA), Color(0xFF4FC3F7))
        "dragon" -> listOf(Color(0xFF7C5CDA), Color(0xFF6240C2))
        "steel" -> listOf(Color(0xFF9EA3A8), Color(0xFF81868B))
        else -> listOf(Color(0xFF95A5A6), Color(0xFF7F8C8D))
    }
}

/**
 * Mendapatkan warna bar statistik Pokémon
 */
fun getStatColor(statName: String): Color {
    return when (statName.lowercase()) {
        "hp" -> StatHpColor
        "attack" -> StatAttackColor
        "defense" -> StatDefenseColor
        "special-attack" -> StatSpAtkColor
        "special-defense" -> StatSpDefColor
        "speed" -> StatSpeedColor
        else -> Color(0xFF4FC3F7)
    }
}