package com.uvg.mypokedex.ui.features.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.Pokemon
import kotlinx.serialization.json.Json
import com.uvg.mypokedex.data.local.DataStoreManager
import com.uvg.mypokedex.data.model.PokemonJsonResponse
import com.uvg.mypokedex.data.model.toPokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// application, en vez de activity, para obtener contexto
// sino se puede morir el activity, pero el viewmodel no, creando memory leak
import com.uvg.mypokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val dataStoreManager = DataStoreManager(application)
    private val repository = PokemonRepository(application)

    // Estado de conexión
    val isConnected: StateFlow<Boolean> = repository.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Lista de Pokémon desde Room (caché local)
    private val cachedPokemonFlow: Flow<List<Pokemon>> = repository.getCachedPokemon()

    // Orden de clasificación actual
    private val _currentSortOrder = MutableStateFlow("BY_NUMBER_ASC")
    val currentSortOrder: StateFlow<String> = _currentSortOrder.asStateFlow()

    // Lista de Pokémon ordenada y filtrada
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList.asStateFlow()

    // Query de búsqueda
    private val _searchQuery = MutableStateFlow("")

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mensaje de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Variables para paginación (desde JSON local)
    private var currentPage = 0
    private val pageSize = 10
    private val pokemonFromJson = mutableListOf<Pokemon>()

    init {
        loadSavedSortOrder()
        observeCachedPokemon()
        initializeData()
    }

    /**
     * Observa cambios en el caché y aplica orden/búsqueda
     */
    private fun observeCachedPokemon() {
        viewModelScope.launch {
            combine(
                cachedPokemonFlow,
                _currentSortOrder,
                _searchQuery
            ) { cached, sortOrder, query ->
                Triple(cached, sortOrder, query)
            }.collect { (cached, sortOrder, query) ->
                val filtered = if (query.isBlank()) {
                    cached
                } else {
                    cached.filter {
                        it.name.contains(query, ignoreCase = true) ||
                                it.id.toString().contains(query)
                    }
                }
                _pokemonList.value = applySorting(filtered, sortOrder)
            }
        }
    }

    /**
     * Inicializa los datos: carga desde JSON y sincroniza con Room
     */
    private fun initializeData() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                android.util.Log.d("HomeViewModel", "Iniciando carga de datos")
                val cacheIsEmpty = repository.isCacheEmpty()
                android.util.Log.d("HomeViewModel", "Cache vacío: $cacheIsEmpty")

                if (cacheIsEmpty) {
                    loadMorePokemonFromJson()

                    if (pokemonFromJson.isNotEmpty()) {
                        android.util.Log.d("HomeViewModel", "Guardando ${pokemonFromJson.size} Pokemon en cache")
                        repository.cachePokemonList(pokemonFromJson)
                    }
                }

                if (isConnected.value) {
                    android.util.Log.d("HomeViewModel", "Intentando refrescar desde API")
                    repository.refreshCacheIfNeeded(forceRefresh = false)
                }

            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Error en initializeData", e)
                _errorMessage.value = "Error al cargar datos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Carga más Pokémon desde JSON local
     */
    fun loadMorePokemonFromJson() {
        try {
            val fileName = getFileNameForPage(currentPage)
            val jsonString = loadJsonFromAssets(fileName)

            // Cambia esta línea:
            val response = Json.decodeFromString<PokemonJsonResponse>(jsonString)
            val newPokemons = response.items.map { it.toPokemon() }

            pokemonFromJson.addAll(newPokemons)
            currentPage++

            viewModelScope.launch {
                repository.cachePokemonList(newPokemons)
            }

        } catch (e: Exception) {
            _errorMessage.value = "Error al cargar Pokémon: ${e.message}"
            android.util.Log.e("HomeViewModel", "Error loading Pokemon", e)
        }
    }

    /**
     * Actualiza la query de búsqueda
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Cambia el orden de clasificación
     */
    fun changeSortOrder(newOrder: String) {
        viewModelScope.launch {
            dataStoreManager.saveSortOrder(newOrder)
        }
    }

    /**
     * Fuerza un refresco desde la API (si hay conexión)
     */
    fun forceRefresh() {
        viewModelScope.launch {
            if (!isConnected.value) {
                _errorMessage.value = "No hay conexión a internet"
                return@launch
            }

            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.refreshCacheIfNeeded(forceRefresh = true)

            when (result) {
                is com.uvg.mypokedex.data.repository.Result.Error -> {
                    _errorMessage.value = result.message
                }
                else -> {
                    // Éxito - el Flow de cachedPokemonFlow actualizará automáticamente
                }
            }

            _isLoading.value = false
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _errorMessage.value = null
    }

    // ===== MÉTODOS PRIVADOS =====

    private fun loadSavedSortOrder() {
        viewModelScope.launch {
            dataStoreManager.sortOrderFlow.collect { savedOrder ->
                _currentSortOrder.value = savedOrder
            }
        }
    }

    private fun applySorting(pokemons: List<Pokemon>, order: String): List<Pokemon> {
        return when (order) {
            "BY_NUMBER_ASC" -> pokemons.sortedBy { it.id }
            "BY_NUMBER_DESC" -> pokemons.sortedByDescending { it.id }
            "BY_NAME_ASC" -> pokemons.sortedBy { it.name }
            "BY_NAME_DESC" -> pokemons.sortedByDescending { it.name }
            else -> pokemons.sortedBy { it.id }
        }
    }

    private fun getFileNameForPage(page: Int): String {
        val start = page * pageSize + 1
        val end = (page + 1) * pageSize
        return "pokemon_${start.toString().padStart(3, '0')}_${end.toString().padStart(3, '0')}.json"
    }

    private fun loadJsonFromAssets(fileName: String): String {
        val inputStream = getApplication<Application>().assets.open(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }



    fun getPokemons(): List<Pokemon> {
        return _pokemonList.value
    }
}

