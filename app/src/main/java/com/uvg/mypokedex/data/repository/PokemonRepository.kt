package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.data.remote.RetrofitClient
import com.uvg.mypokedex.data.remote.model.PokemonDetailResponse
import com.uvg.mypokedex.data.remote.model.PokemonListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepository {
    private val apiService = RetrofitClient.pokeApiService

    suspend fun getPokemonList(limit: Int = 20, offset: Int = 0): Flow<Result<PokemonListResponse>> = flow {
        try {
            emit(Result.loading())
            val response = apiService.getPokemonList(limit, offset)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.error("Error fetching Pokémon list: ${e.message}"))
        }
    }

    suspend fun getPokemonDetail(id: Int): Flow<Result<PokemonDetailResponse>> = flow {
        try {
            emit(Result.loading())
            val response = apiService.getPokemonDetail(id)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.error("Error fetching Pokémon detail: ${e.message}"))
        }
    }
}

// Clase auxiliar para manejar estados
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun error(message: String): Result<Nothing> = Error(message)
        fun loading(): Result<Nothing> = Loading
    }
}