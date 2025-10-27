package com.uvg.mypokedex.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.PokemonDetail
import com.uvg.mypokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PokemonDetailState {
    object Loading : PokemonDetailState()
    data class Success(val pokemon: PokemonDetail) : PokemonDetailState()
    data class Error(val message: String) : PokemonDetailState()
}

class PokemonDetailViewModel : ViewModel() {
    private val repository = PokemonRepository()
    private val _state = MutableStateFlow<PokemonDetailState>(PokemonDetailState.Loading)
    val state: StateFlow<PokemonDetailState> = _state.asStateFlow()

    fun loadPokemon(id: Int) {
        viewModelScope.launch {
            _state.value = PokemonDetailState.Loading

            val result = repository.getPokemonDetail(id)

            result.onSuccess { pokemon ->
                _state.value = PokemonDetailState.Success(pokemon)
            }

            result.onFailure { exception ->
                _state.value = PokemonDetailState.Error(
                    exception.message ?: "Error desconocido"
                )
            }
        }
    }

    fun retry(id: Int) {
        loadPokemon(id)
    }
}