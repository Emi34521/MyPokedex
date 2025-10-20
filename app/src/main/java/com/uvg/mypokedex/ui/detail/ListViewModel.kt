package com.uvg.mypokedex.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

/**
 * ViewModel alternativo con arquitectura moderna usando UiState y eventos.
 * Puedes reemplazar HomeViewModel con este cuando estés listo.
 */
class NewHomeViewModel(application: Application) : AndroidViewModel(application) {

    // ===== ESTADO PRIVADO MUTABLE =====
    private val _uiState = MutableStateFlow(HomeUiState())

    // ===== ESTADO PÚBLICO DE SOLO LECTURA =====
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Variables internas para paginación
    private var currentPage = 0
    private val pageSize = 10
    private val allLoadedPokemons = mutableListOf<Pokemon>()

    init {
        loadMorePokemon()
    }

    // ===== MÉTODO PRINCIPAL PARA MANEJAR EVENTOS =====
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Search -> handleSearch(event.query)
            is HomeEvent.LoadMore -> loadMorePokemon()
            is HomeEvent.UpdateSortOrder -> handleSortOrderChange(event.order)
            is HomeEvent.ClearError -> clearError()
            is HomeEvent.Refresh -> refresh()
        }
    }

    // ===== HANDLERS PRIVADOS =====

    private fun handleSearch(query: String) {
        _uiState.update { currentState ->
            val filteredPokemons = if (query.isBlank()) {
                allLoadedPokemons
            } else {
                allLoadedPokemons.filter { pokemon ->
                    pokemon.name.contains(query, ignoreCase = true) ||
                            pokemon.id.toString().contains(query)
                }
            }
            currentState.copy(
                searchQuery = query,
                pokemons = applySorting(filteredPokemons, currentState.sortOrder)
            )
        }
    }

    private fun handleSortOrderChange(order: SortOrder) {
        _uiState.update { currentState ->
            currentState.copy(
                sortOrder = order,
                pokemons = applySorting(currentState.pokemons, order)
            )
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun refresh() {
        currentPage = 0
        allLoadedPokemons.clear()
        _uiState.update {
            HomeUiState(
                searchQuery = it.searchQuery,
                sortOrder = it.sortOrder
            )
        }
        loadMorePokemon()
    }

    private fun applySorting(pokemons: List<Pokemon>, order: SortOrder): List<Pokemon> {
        return when (order) {
            SortOrder.BY_NUMBER_ASC -> pokemons.sortedBy { it.id }
            SortOrder.BY_NUMBER_DESC -> pokemons.sortedByDescending { it.id }
            SortOrder.BY_NAME_ASC -> pokemons.sortedBy { it.name }
            SortOrder.BY_NAME_DESC -> pokemons.sortedByDescending { it.name }
        }
    }

    // ===== CARGA DE DATOS =====

    fun loadMorePokemon() {
        // Evitar múltiples cargas simultáneas
        if (_uiState.value.isLoading || !_uiState.value.hasMorePages) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val fileName = getFileNameForPage(currentPage)
                val jsonString = loadJsonFromAssets(fileName)
                val newPokemons = Json.decodeFromString<List<Pokemon>>(jsonString)

                allLoadedPokemons.addAll(newPokemons)
                currentPage++

                _uiState.update { currentState ->
                    val filteredPokemons = if (currentState.searchQuery.isBlank()) {
                        allLoadedPokemons
                    } else {
                        allLoadedPokemons.filter { pokemon ->
                            pokemon.name.contains(currentState.searchQuery, ignoreCase = true) ||
                                    pokemon.id.toString().contains(currentState.searchQuery)
                        }
                    }
                    currentState.copy(
                        pokemons = applySorting(filteredPokemons, currentState.sortOrder),
                        isLoading = false,
                        hasMorePages = newPokemons.size == pageSize
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudieron cargar más Pokémon: ${e.message}",
                        hasMorePages = false
                    )
                }
            }
        }
    }

    private fun getFileNameForPage(page: Int): String {
        val start = page * pageSize + 1
        val end = (page + 1) * pageSize
        return "pokemon_${start.toString().padStart(3, '0')}_${end}.json"
    }

    private fun loadJsonFromAssets(fileName: String): String {
        val inputStream = getApplication<Application>().assets.open(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }

    // ===== MÉTODOS DE COMPATIBILIDAD (para código existente) =====

    fun getPokemons(): List<Pokemon> {
        return _uiState.value.pokemons
    }
}