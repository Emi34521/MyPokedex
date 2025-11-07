package com.uvg.mypokedex.data.repository

import android.util.Log
import com.uvg.mypokedex.data.local.CachedPokemon
import com.uvg.mypokedex.data.local.PokemonDao
import com.uvg.mypokedex.data.local.PokemonStat
import com.uvg.mypokedex.data.network.PokemonApiService
import com.uvg.mypokedex.data.preferences.SortOrder
import com.uvg.mypokedex.data.preferences.UserPreferencesRepository
import com.uvg.mypokedex.util.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.flatMapLatest

class PokemonRepository(
    private val pokemonDao: PokemonDao,
    private val apiService: PokemonApiService,
    private val preferencesRepository: UserPreferencesRepository,
    private val connectivityObserver: ConnectivityObserver
) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Tiempo de caché: 1 hora
    private val CACHE_TIMEOUT_MS = TimeUnit.HOURS.toMillis(1)

    // Flow de conectividad
    val isConnected: StateFlow<Boolean> = connectivityObserver.observe()
        .stateIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = connectivityObserver.isConnected()
        )

    // Flow de orden de clasificación
    val sortOrder: Flow<SortOrder> = preferencesRepository.sortOrderFlow

    // Flow combinado de Pokémon ordenados según preferencia
    @OptIn(ExperimentalCoroutinesApi::class)
    val pokemonList: Flow<List<CachedPokemon>> = combine(sortOrder) { order ->
        when (order.first()) {
            SortOrder.NUMBER_ASC -> pokemonDao.getAllPokemonByNumberAsc()
            SortOrder.NUMBER_DESC -> pokemonDao.getAllPokemonByNumberDesc()
            SortOrder.NAME_ASC -> pokemonDao.getAllPokemonByNameAsc()
            SortOrder.NAME_DESC -> pokemonDao.getAllPokemonByNameDesc()
        }
    }.flatMapLatest { flow ->
        flow
    }.stateIn(
        scope = repositoryScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList() // Adjusted to match List<CachedPokemon> instead of Flow
    ).let { stateFlow ->
        var currentFlow: Flow<List<CachedPokemon>>? = null
        combine(sortOrder, stateFlow) { order ->
            // Get new flow based on order
            val newFlow = when (order.first()) {
                SortOrder.NUMBER_ASC -> pokemonDao.getAllPokemonByNumberAsc()
                SortOrder.NUMBER_DESC -> pokemonDao.getAllPokemonByNumberDesc()
                SortOrder.NAME_ASC -> pokemonDao.getAllPokemonByNameAsc()
                SortOrder.NAME_DESC -> pokemonDao.getAllPokemonByNameDesc()
                else -> emptyFlow()
            }
            // Update currentFlow only if it is different
            if (currentFlow != newFlow) {
                currentFlow = newFlow
            }
            // Return the current flow
            currentFlow
        }.flatMapLatest { flow ->
            flow // Ensure the flow is of type Flow<List<CachedPokemon>>
        }
    }



    // Método simplificado para obtener Pokémon según orden
    fun getPokemonListByOrder(order: SortOrder): Flow<List<CachedPokemon>> {
        return when (order) {
            SortOrder.NUMBER_ASC -> pokemonDao.getAllPokemonByNumberAsc()
            SortOrder.NUMBER_DESC -> pokemonDao.getAllPokemonByNumberDesc()
            SortOrder.NAME_ASC -> pokemonDao.getAllPokemonByNameAsc()
            SortOrder.NAME_DESC -> pokemonDao.getAllPokemonByNameDesc()
        }
    }

    init {
        // Observar conectividad para refrescar caché automáticamente
        repositoryScope.launch {
            isConnected.collect { connected ->
                if (connected) {
                    refreshCacheIfNeeded()
                }
            }
        }
    }

    suspend fun saveSortOrder(order: SortOrder) {
        preferencesRepository.saveSortOrder(order)
    }

    suspend fun refreshCacheIfNeeded() {
        withContext(Dispatchers.IO) {
            try {
                // Verificar si hay conexión
                if (!connectivityObserver.isConnected()) {
                    Log.d("PokemonRepository", "No hay conexión, usando caché")
                    return@withContext
                }

                // Verificar si el caché está desactualizado
                val lastFetchTime = pokemonDao.getLastFetchTime() ?: 0
                val currentTime = System.currentTimeMillis()
                val cacheIsOld = (currentTime - lastFetchTime) > CACHE_TIMEOUT_MS

                // Verificar si la base de datos está vacía
                val pokemonCount = pokemonDao.getPokemonCount()

                if (pokemonCount == 0 || cacheIsOld) {
                    Log.d("PokemonRepository", "Refrescando caché desde la API")
                    fetchAndCachePokemon()
                } else {
                    Log.d("PokemonRepository", "Caché aún válido")
                }
            } catch (e: Exception) {
                Log.e("PokemonRepository", "Error al refrescar caché", e)
            }
        }
    }

    private suspend fun fetchAndCachePokemon() {
        try {
            // Obtener lista de Pokémon
            val pokemonListResponse = apiService.getPokemonList(limit = 151)

            val cachedPokemonList = pokemonListResponse.results.mapIndexed { index, item ->
                try {
                    // Extraer ID de la URL
                    val id = item.url.trimEnd('/').split('/').last().toInt()

                    // Obtener detalles del Pokémon
                    val details = apiService.getPokemonDetail(id)

                    // Obtener la mejor imagen disponible
                    val imageUrl = details.sprites.other?.officialArtwork?.frontDefault
                        ?: details.sprites.frontDefault
                        ?: ""

                    // Extraer tipos
                    val types = details.types.map { it.type.name }

                    // Extraer stats
                    val stats = details.stats.map {
                        PokemonStat(
                            name = it.stat.name,
                            value = it.base_stat
                        )
                    }

                    CachedPokemon(
                        id = details.id,
                        name = details.name,
                        imageUrl = imageUrl,
                        types = types,
                        stats = stats,
                        lastFetchedAt = System.currentTimeMillis()
                    )
                } catch (e: Exception) {
                    Log.e("PokemonRepository", "Error al obtener detalles del Pokémon ${item.name}", e)
                    null
                }
            }.filterNotNull()

            // Guardar en la base de datos
            pokemonDao.insertAllPokemon(cachedPokemonList)
            Log.d("PokemonRepository", "Caché actualizado con ${cachedPokemonList.size} Pokémon")

        } catch (e: Exception) {
            Log.e("PokemonRepository", "Error al obtener datos de la API", e)
            throw e
        }
    }

    fun getPokemonById(id: Int): Flow<CachedPokemon?> {
        return pokemonDao.getPokemonById(id)
    }

    suspend fun forceRefresh() {
        if (connectivityObserver.isConnected()) {
            fetchAndCachePokemon()
        }
    }
}