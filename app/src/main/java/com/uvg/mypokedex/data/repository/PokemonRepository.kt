package com.uvg.mypokedex.data.repository

import android.content.Context
import com.uvg.mypokedex.data.local.PokemonDatabase
import com.uvg.mypokedex.data.local.PokemonDao
import com.uvg.mypokedex.data.toCachedPokemon
import com.uvg.mypokedex.data.toPokemonList
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.NetworkMonitor
import com.uvg.mypokedex.data.remote.RetrofitClient
import com.uvg.mypokedex.data.toPokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class PokemonRepository(context: Context) {

    private val pokemonDao: PokemonDao = PokemonDatabase.getInstance(context).pokemonDao()
    private val networkMonitor = NetworkMonitor(context)
    private val apiService = RetrofitClient.pokeApiService

    // Tiempo de expiración del caché (24 horas)
    private val cacheExpirationTime = TimeUnit.HOURS.toMillis(24)

    /**
     * Flow que emite el estado de conexión a internet
     */
    val isConnected: Flow<Boolean> = networkMonitor.isConnected

    /**
     * Obtiene los Pokémon desde el caché local (Room)
     * Este Flow emite automáticamente cuando hay cambios en la DB
     */
    fun getCachedPokemon(): Flow<List<Pokemon>> {
        return pokemonDao.getAllPokemon().map { cachedList ->
            cachedList.toPokemonList()
        }
    }

    /**
     * Verifica si el caché está vacío
     */
    suspend fun isCacheEmpty(): Boolean {
        return pokemonDao.getPokemonCount() == 0
    }

    /**
     * Verifica si el caché ha expirado
     */
    suspend fun isCacheExpired(): Boolean {
        val lastFetchTime = pokemonDao.getLastFetchTime() ?: return true
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastFetchTime) > cacheExpirationTime
    }

    /**
     * Refresca el caché desde la API si hay conexión
     * @param forceRefresh Si es true, refresca incluso si el caché no ha expirado
     */
    suspend fun refreshCacheIfNeeded(forceRefresh: Boolean = false): Result<Unit> {
        // Verificar si hay conexión
        val hasConnection = networkMonitor.isConnected.first()
        if (!hasConnection) {
            return Result.Error("No hay conexión a internet")
        }

        // Verificar si necesita actualización
        val needsRefresh = forceRefresh || isCacheEmpty() || isCacheExpired()
        if (!needsRefresh) {
            return Result.Success(Unit)
        }

        return try {
            // Obtener datos de la API (primeras 50 páginas como ejemplo)
            val pokemonList = mutableListOf<Pokemon>()

            // Aquí deberías cargar desde tu JSON local o API
            // Por ahora, asumimos que ya tienes la lista cargada
            // Esta es una implementación simplificada

            // Si tienes pokémon en memoria, guárdalos
            if (pokemonList.isNotEmpty()) {
                val cachedPokemons = pokemonList.map { it.toCachedPokemon() }
                pokemonDao.insertAllPokemon(cachedPokemons)
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al refrescar caché: ${e.message}")
        }
    }

    /**
     * Guarda una lista de Pokémon en el caché
     */
    suspend fun cachePokemonList(pokemonList: List<Pokemon>) {
        val cachedPokemons = pokemonList.map { it.toCachedPokemon() }
        pokemonDao.insertAllPokemon(cachedPokemons)
    }

    /**
     * Busca un Pokémon específico por ID
     */
    suspend fun getPokemonById(id: Int): Pokemon? {
        return pokemonDao.getPokemonById(id)?.toPokemon()
    }

    /**
     * Limpia todo el caché
     */
    suspend fun clearCache() {
        pokemonDao.deleteAllPokemon()
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