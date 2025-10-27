package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.data.model.PokemonBasic
import com.uvg.mypokedex.data.model.PokemonDetail
import com.uvg.mypokedex.data.network.RetrofitInstance

class PokemonRepository {
    private val api = RetrofitInstance.api

    suspend fun getPokemonList(offset: Int, limit: Int): Result<List<PokemonBasic>> {
        return try {
            val response = api.getPokemonList(offset, limit)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.results)
            } else {
                Result.failure(Exception("Error al cargar la lista de Pokémon"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail> {
        return try {
            val response = api.getPokemonDetail(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar el detalle del Pokémon"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}