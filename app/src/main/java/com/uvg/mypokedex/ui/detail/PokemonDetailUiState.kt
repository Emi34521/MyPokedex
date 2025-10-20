package com.uvg.mypokedex.ui.detail

import com.uvg.mypokedex.data.model.Pokemon

// ===== ESTADOS PARA HOME =====

data class HomeUiState(
    val pokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.BY_NUMBER_ASC,
    val errorMessage: String? = null,
    val hasMorePages: Boolean = true
)

enum class SortOrder {
    BY_NUMBER_ASC,
    BY_NUMBER_DESC,
    BY_NAME_ASC,
    BY_NAME_DESC
}

// ===== ESTADOS PARA DETALLE =====

data class DetailUiState(
    val pokemon: Pokemon? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)