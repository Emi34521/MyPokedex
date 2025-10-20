package com.uvg.mypokedex.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel alternativo para el detalle usando UiState y eventos.
 * Puedes reemplazar PokemonDetailViewModel con este cuando estés listo.
 */
class NewDetailViewModel : ViewModel() {

    // ===== ESTADO PRIVADO MUTABLE =====
    private val _uiState = MutableStateFlow(DetailUiState())

    // ===== ESTADO PÚBLICO DE SOLO LECTURA =====
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    // Lista simulada de favoritos (en producción esto vendría de una BD o DataStore)
    private val favoritesSet = mutableSetOf<Int>()

    // ===== INICIALIZACIÓN =====

    fun initializePokemon(pokemon: Pokemon) {
        _uiState.update {
            it.copy(
                pokemon = pokemon,
                isFavorite = favoritesSet.contains(pokemon.id),
                isLoading = false
            )
        }
    }

    // ===== MÉTODO PRINCIPAL PARA MANEJAR EVENTOS =====

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.ToggleFavorite -> handleToggleFavorite()
            is DetailEvent.NavigateBack -> {} // Manejado por la UI
            is DetailEvent.Refresh -> handleRefresh()
        }
    }

    // ===== HANDLERS PRIVADOS =====

    private fun handleToggleFavorite() {
        viewModelScope.launch {
            val currentPokemon = _uiState.value.pokemon ?: return@launch

            val newFavoriteState = !_uiState.value.isFavorite

            // Actualizar el set de favoritos
            if (newFavoriteState) {
                favoritesSet.add(currentPokemon.id)
            } else {
                favoritesSet.remove(currentPokemon.id)
            }

            // Actualizar el estado
            _uiState.update { currentState ->
                currentState.copy(isFavorite = newFavoriteState)
            }

            // Aquí podrías guardar en una base de datos o SharedPreferences
            // saveFavoriteToDatabase(currentPokemon.id, newFavoriteState)
        }
    }

    private fun handleRefresh() {
        val currentPokemon = _uiState.value.pokemon ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Aquí podrías recargar datos desde la API si fuera necesario
                // Por ahora solo actualizamos el estado de favorito
                _uiState.update {
                    it.copy(
                        isFavorite = favoritesSet.contains(currentPokemon.id),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al actualizar: ${e.message}"
                    )
                }
            }
        }
    }

    // ===== MÉTODOS DE COMPATIBILIDAD (para código existente) =====

    fun getIsFavourite(): Boolean {
        return _uiState.value.isFavorite
    }

    // ===== MÉTODOS PÚBLICOS ADICIONALES =====

    fun isFavorite(pokemonId: Int): Boolean {
        return favoritesSet.contains(pokemonId)
    }

    fun getAllFavorites(): Set<Int> {
        return favoritesSet.toSet()
    }
}