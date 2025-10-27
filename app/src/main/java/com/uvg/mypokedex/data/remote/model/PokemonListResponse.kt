package com.uvg.mypokedex.data.remote.model

// Modelo de datos para la respuesta de la API de Pokémon
data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
)