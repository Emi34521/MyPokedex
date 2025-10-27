package com.uvg.mypokedex.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.PokemonBasic
import com.uvg.mypokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PokemonListState {
    object Loading : PokemonListState()
    data class Success(val pokemons: List<PokemonBasic>, val hasMore: Boolean) : PokemonListState()
    data class Error(val message: String) : PokemonListState()
}

class PokemonListViewModel : ViewModel() {
    private val repository = PokemonRepository()
    private val _state = MutableStateFlow<PokemonListState>(PokemonListState.Loading)
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    private val allPokemons = mutableListOf<PokemonBasic>()
    private var currentOffset = 0
    private val pageSize = 20

    init {
        loadPokemons()
    }

    fun loadPokemons() {
        viewModelScope.launch {
            _state.value = PokemonListState.Loading

            val result = repository.getPokemonList(currentOffset, pageSize)

            result.onSuccess { newPokemons ->
                allPokemons.addAll(newPokemons)
                currentOffset += pageSize
                _state.value = PokemonListState.Success(
                    pokemons = allPokemons.toList(),
                    hasMore = newPokemons.size == pageSize
                )
            }

            result.onFailure { exception ->
                _state.value = PokemonListState.Error(
                    exception.message ?: "Error desconocido"
                )
            }
        }
    }

    fun loadMore() {
        if (_state.value is PokemonListState.Success) {
            viewModelScope.launch {
                val result = repository.getPokemonList(currentOffset, pageSize)

                result.onSuccess { newPokemons ->
                    allPokemons.addAll(newPokemons)
                    currentOffset += pageSize
                    _state.value = PokemonListState.Success(
                        pokemons = allPokemons.toList(),
                        hasMore = newPokemons.size == pageSize
                    )
                }
            }
        }
    }

    fun retry() {
        currentOffset = 0
        allPokemons.clear()
        loadPokemons()
    }
}