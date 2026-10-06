package com.najmizahrian.pokedex.data.model

/**
 * Helper untuk menghitung efektivitas tipe (Type Effectiveness / Advantage)
 * pada simulasi pertarungan Pokémon
 */
object TypeChartHelper {

    // Daftar semua 18 tipe Pokémon resmi
    val allTypes = listOf(
        "normal", "fire", "water", "grass", "electric", "ice",
        "fighting", "poison", "ground", "flying", "psychic", "bug",
        "rock", "ghost", "dragon", "steel", "fairy", "dark"
    )

    // Pemetaan keunggulan tipe (Attacker -> Defender yang menerima damage 2x / Super Effective)
    private val superEffectiveMap: Map<String, List<String>> = mapOf(
        "fire" to listOf("grass", "ice", "bug", "steel"),
        "water" to listOf("fire", "ground", "rock"),
        "grass" to listOf("water", "ground", "rock"),
        "electric" to listOf("water", "flying"),
        "ice" to listOf("grass", "ground", "flying", "dragon"),
        "fighting" to listOf("normal", "ice", "rock", "dark", "steel"),
        "poison" to listOf("grass", "fairy"),
        "ground" to listOf("fire", "electric", "poison", "rock", "steel"),
        "flying" to listOf("grass", "fighting", "bug"),
        "psychic" to listOf("fighting", "poison"),
        "bug" to listOf("grass", "psychic", "dark"),
        "rock" to listOf("fire", "ice", "flying", "bug"),
        "ghost" to listOf("psychic", "ghost"),
        "dragon" to listOf("dragon"),
        "steel" to listOf("ice", "rock", "fairy"),
        "fairy" to listOf("fighting", "dragon", "dark"),
        "dark" to listOf("psychic", "ghost")
    )

    // Pemetaan resistensi tipe (Defender -> Attacker yang serangannya resisted 0.5x)
    private val resistanceMap: Map<String, List<String>> = mapOf(
        "fire" to listOf("fire", "grass", "ice", "bug", "steel", "fairy"),
        "water" to listOf("fire", "water", "ice", "steel"),
        "grass" to listOf("water", "electric", "grass", "ground"),
        "electric" to listOf("electric", "flying", "steel"),
        "ice" to listOf("ice"),
        "fighting" to listOf("bug", "rock", "dark"),
        "poison" to listOf("grass", "fighting", "poison", "bug", "fairy"),
        "ground" to listOf("poison", "rock"),
        "flying" to listOf("grass", "fighting", "bug"),
        "psychic" to listOf("fighting", "psychic"),
        "bug" to listOf("grass", "fighting", "ground"),
        "rock" to listOf("normal", "fire", "poison", "flying"),
        "ghost" to listOf("poison", "bug"),
        "dragon" to listOf("fire", "water", "electric", "grass"),
        "steel" to listOf("normal", "grass", "ice", "flying", "psychic", "bug", "rock", "dragon", "steel", "fairy"),
        "fairy" to listOf("fighting", "bug", "dark"),
        "dark" to listOf("ghost", "dark"),
        "normal" to emptyList()
    )

    /**
     * Memeriksa apakah attackerType memiliki keunggulan atas defenderType
     */
    fun isSuperEffective(attackerType: String, defenderType: String): Boolean {
        return superEffectiveMap[attackerType.lowercase()]?.contains(defenderType.lowercase()) == true
    }

    /**
     * Memeriksa apakah defenderType menahan (resists) serangan dari attackerType
     */
    fun isResistant(defenderType: String, attackerType: String): Boolean {
        return resistanceMap[defenderType.lowercase()]?.contains(attackerType.lowercase()) == true
    }

    /**
     * Menghitung cakupan tipe ofensif tim (seluruh tipe musuh yang bisa diserang super-effective)
     */
    fun calculateTeamCoverage(teamTypes: List<List<String>>): List<String> {
        val coverageSet = mutableSetOf<String>()
        val distinctAttackerTypes = teamTypes.flatten().map { it.lowercase() }.distinct()

        for (attacker in distinctAttackerTypes) {
            superEffectiveMap[attacker]?.let { targets ->
                coverageSet.addAll(targets)
            }
        }
        return coverageSet.toList()
    }

    /**
     * Menghitung frekuensi kerentanan tim terhadap tiap tipe penyerang musuh
     * Mengembalikan map tipe penyerang -> jumlah Pokémon yang rentan (lemah)
     */
    fun calculateTeamWeaknessFrequency(teamTypes: List<List<String>>): Map<String, Int> {
        val weaknessFreq = mutableMapOf<String, Int>()

        for (incomingType in allTypes) {
            var weakCount = 0
            for (pokemonTypes in teamTypes) {
                // Pokémon dianggap lemah jika salah satu tipenya menerima super effective
                // dan tidak ada tipe kedua yang menahan serangan tsb
                val isWeak = pokemonTypes.any { defType -> isSuperEffective(incomingType, defType) }
                val isResist = pokemonTypes.any { defType -> isResistant(defType, incomingType) }

                if (isWeak && !isResist) {
                    weakCount++
                }
            }
            if (weakCount > 0) {
                weaknessFreq[incomingType] = weakCount
            }
        }
        return weaknessFreq.toList().sortedByDescending { it.second }.toMap()
    }

    /**
     * Menghitung skor sinergi tim berdasarkan cakupan elemen dan keragaman statistik (0 - 100)
     */
    fun calculateSynergyScore(memberCount: Int, coveredTypesCount: Int, totalWeaknessCount: Int): Pair<Int, String> {
        if (memberCount == 0) return Pair(0, "-")

        // Proporsi cakupan elemen (maks 18 tipe)
        val coverageRatio = (coveredTypesCount.toFloat() / 18f).coerceIn(0f, 1f)
        val penalty = (totalWeaknessCount * 2).coerceAtMost(30)
        val baseScore = (coverageRatio * 75f + (memberCount.toFloat() / 6f) * 25f - penalty).toInt().coerceIn(10, 100)

        val rank = when {
            baseScore >= 85 -> "S (Master Tier)"
            baseScore >= 70 -> "A (Elite Tier)"
            baseScore >= 55 -> "B (Solid Tier)"
            baseScore >= 40 -> "C (Casual Tier)"
            else -> "D (Developing)"
        }

        return Pair(baseScore, rank)
    }
}
