package com.uvg.mypokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.local.CachedPokemon
import com.uvg.mypokedex.data.preferences.SortOrder
import com.uvg.mypokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PokemonListUiState(
    val pokemon: List<CachedPokemon> = emptyList(),
    val isLoading: Boolean = false,
    val isConnected: Boolean = false,
    val sortOrder: SortOrder = SortOrder.NUMBER_ASC,
    val error: String? = null
)

class PokemonViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    // Combinar todos los flujos en un único estado
    val combinedUiState: StateFlow<PokemonListUiState> = combine(
        repository.sortOrder,
        repository.isConnected,
        _uiState
    ) { sortOrder, isConnected, currentState ->
        currentState.copy(
            sortOrder = sortOrder,
            isConnected = isConnected
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PokemonListUiState()
    )

    init {
        loadPokemon()
    }

    private fun loadPokemon() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Refrescar caché si es necesario
                repository.refreshCacheIfNeeded()

                // Observar cambios en el orden de clasificación
                repository.sortOrder.collect { order ->
                    // Obtener Pokémon según el orden actual
                    repository.getPokemonListByOrder(order).collect { pokemonList ->
                        _uiState.value = _uiState.value.copy(
                            pokemon = pokemonList,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun setSortOrder(order: SortOrder) {
        viewModelScope.launch {
            try {
                repository.saveSortOrder(order)
                // El flujo se actualizará automáticamente
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al guardar preferencia: ${e.message}"
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                repository.forceRefresh()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al actualizar: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

class PokemonViewModelFactory(
    private val repository: PokemonRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}