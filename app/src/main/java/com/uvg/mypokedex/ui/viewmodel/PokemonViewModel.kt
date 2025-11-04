package com.uvg.mypokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.local.CachedPokemon
import com.uvg.mypokedex.data.preferences.SortOrder
import com.uvg.mypokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.*
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

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    // Combinar todos los flujos en un único estado
    val combinedUiState: StateFlow<PokemonListUiState> = combine(
        repository.sortOrder,
        repository.isConnected,
        _isLoading,
        _error
    ) { sortOrder, isConnected, isLoading, error ->
        sortOrder to Triple(isConnected, isLoading, error)
    }.flatMapLatest { (sortOrder, triple) ->
        val (isConnected, isLoading, error) = triple
        repository.getPokemonListByOrder(sortOrder).map { pokemonList ->
            PokemonListUiState(
                pokemon = pokemonList,
                isLoading = isLoading,
                isConnected = isConnected,
                sortOrder = sortOrder,
                error = error
            )
        }
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
            _isLoading.value = true

            try {
                // Refrescar caché si es necesario
                repository.refreshCacheIfNeeded()
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message
            }
        }
    }

    fun setSortOrder(order: SortOrder) {
        viewModelScope.launch {
            try {
                repository.saveSortOrder(order)
            } catch (e: Exception) {
                _error.value = "Error al guardar preferencia: ${e.message}"
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                repository.forceRefresh()
                _isLoading.value = false
                _error.value = null
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Error al actualizar: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
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