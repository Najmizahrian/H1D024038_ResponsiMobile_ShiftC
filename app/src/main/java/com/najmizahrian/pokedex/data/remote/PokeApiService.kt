package com.najmizahrian.pokedex.data.remote

import com.najmizahrian.pokedex.data.model.PokemonDetailResponse
import com.najmizahrian.pokedex.data.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface Retrofit untuk komunikasi dengan PokéAPI
 */
interface PokeApiService {

    /**
     * Mengambil daftar Pokémon
     * Menggunakan limit 151 untuk generasi 1 klasik
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 151,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    /**
     * Mengambil detail lengkap Pokémon berdasarkan ID atau nama
     */
    @GET("pokemon/{nameOrId}")
    suspend fun getPokemonDetail(
        @Path("nameOrId") nameOrId: String
    ): PokemonDetailResponse
}
