package com.najmizahrian.pokedex.data.model

/**
 * Helper untuk pemetaan tipe utama Pokémon berdasarkan ID resmi (Generasi 1)
 * Digunakan agar kartu Pokémon di Home Screen dapat langsung menampilkan
 * warna latar belakang dan chip tipe sesuai atribut Pokémon secara instan
 */
object PokemonTypeHelper {

    private val typeMap = mapOf(
        // Bulbasaur line
        1 to "grass", 2 to "grass", 3 to "grass",
        // Charmander line
        4 to "fire", 5 to "fire", 6 to "fire",
        // Squirtle line
        7 to "water", 8 to "water", 9 to "water",
        // Caterpie & Weedle lines
        10 to "bug", 11 to "bug", 12 to "bug",
        13 to "bug", 14 to "bug", 15 to "bug",
        // Pidgey & Rattata
        16 to "normal", 17 to "normal", 18 to "normal",
        19 to "normal", 20 to "normal",
        // Spearow & Ekans
        21 to "normal", 22 to "normal",
        23 to "poison", 24 to "poison",
        // Pikachu & Sandshrew
        25 to "electric", 26 to "electric",
        27 to "ground", 28 to "ground",
        // Nidoran lines
        29 to "poison", 30 to "poison", 31 to "poison",
        32 to "poison", 33 to "poison", 34 to "poison",
        // Clefairy & Vulpix
        35 to "fairy", 36 to "fairy",
        37 to "fire", 38 to "fire",
        // Jigglypuff & Zubat
        39 to "normal", 40 to "normal",
        41 to "poison", 42 to "poison",
        // Oddish & Paras
        43 to "grass", 44 to "grass", 45 to "grass",
        46 to "bug", 47 to "bug",
        // Venonat & Diglett
        48 to "bug", 49 to "bug",
        50 to "ground", 51 to "ground",
        // Meowth & Psyduck
        52 to "normal", 53 to "normal",
        54 to "water", 55 to "water",
        // Mankey & Growlithe
        56 to "fighting", 57 to "fighting",
        58 to "fire", 59 to "fire",
        // Poliwag & Abra
        60 to "water", 61 to "water", 62 to "water",
        63 to "psychic", 64 to "psychic", 65 to "psychic",
        // Machop & Bellsprout
        66 to "fighting", 67 to "fighting", 68 to "fighting",
        69 to "grass", 70 to "grass", 71 to "grass",
        // Tentacool & Geodude
        72 to "water", 73 to "water",
        74 to "rock", 75 to "rock", 76 to "rock",
        // Ponyta & Slowpoke
        77 to "fire", 78 to "fire",
        79 to "water", 80 to "water",
        // Magnemite & Farfetch'd
        81 to "electric", 82 to "electric",
        83 to "normal",
        // Doduo & Seel
        84 to "normal", 85 to "normal",
        86 to "water", 87 to "water",
        // Grimer & Shellder
        88 to "poison", 89 to "poison",
        90 to "water", 91 to "water",
        // Gastly & Onix
        92 to "ghost", 93 to "ghost", 94 to "ghost",
        95 to "rock",
        // Drowzee & Krabby
        96 to "psychic", 97 to "psychic",
        98 to "water", 99 to "water",
        // Voltorb & Exeggcute
        100 to "electric", 101 to "electric",
        102 to "grass", 103 to "grass",
        // Cubone & Hitmons
        104 to "ground", 105 to "ground",
        106 to "fighting", 107 to "fighting",
        // Lickitung & Koffing
        108 to "normal",
        109 to "poison", 110 to "poison",
        // Rhyhorn & Chansey & Tangela
        111 to "ground", 112 to "ground",
        113 to "normal",
        114 to "grass",
        // Kangaskhan & Horsea & Goldeen
        115 to "normal",
        116 to "water", 117 to "water",
        118 to "water", 119 to "water",
        // Staryu & Mr. Mime & Scyther
        120 to "water", 121 to "water",
        122 to "psychic",
        123 to "bug",
        // Jynx & Electabuzz & Magmar & Pinsir & Tauros
        124 to "ice",
        125 to "electric",
        126 to "fire",
        127 to "bug",
        128 to "normal",
        // Magikarp & Lapras & Ditto & Eevee
        129 to "water", 130 to "water",
        131 to "water",
        132 to "normal",
        133 to "normal", 134 to "water", 135 to "electric", 136 to "fire",
        // Porygon & Fossils
        137 to "normal",
        138 to "rock", 139 to "rock",
        140 to "rock", 141 to "rock",
        142 to "rock",
        // Snorlax & Birds & Dragons & Mew
        143 to "normal",
        144 to "ice", 145 to "electric", 146 to "fire",
        147 to "dragon", 148 to "dragon", 149 to "dragon",
        150 to "psychic", 151 to "psychic"
    )

    fun getTypeForId(id: Int): String {
        return typeMap[id] ?: "normal"
    }
}
