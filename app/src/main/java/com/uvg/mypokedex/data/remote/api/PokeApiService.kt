package com.uvg.mypokedex.data.remote.api

import com.uvg.mypokedex.data.remote.model.PokemonDetailResponse
import com.uvg.mypokedex.data.remote.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    // obtener lista de Pokémon
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    // obtener detalles de un pokemon
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailResponse
}