package com.najmizahrian.pokedex.ui.navigation

/**
 * Rute navigasi aplikasi
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")

    data object Detail : Screen("detail_screen/{pokemonId}") {
        const val ARG_POKEMON_ID = "pokemonId"

        fun createRoute(pokemonId: Int): String {
            return "detail_screen/$pokemonId"
        }
    }

    data object Team : Screen("team_screen") {
        fun createRoute(): String = "team_screen"
    }
}
