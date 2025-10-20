package com.uvg.mypokedex.ui.detail

// ===== EVENTOS DE USUARIO PARA HOME =====

sealed class HomeEvent {
    data class Search(val query: String) : HomeEvent()
    object LoadMore : HomeEvent()
    data class UpdateSortOrder(val order: SortOrder) : HomeEvent()
    object ClearError : HomeEvent()
    object Refresh : HomeEvent()
}

// ===== EVENTOS DE USUARIO PARA DETALLE =====

sealed class DetailEvent {
    object ToggleFavorite : DetailEvent()
    object NavigateBack : DetailEvent()
    object Refresh : DetailEvent()
}