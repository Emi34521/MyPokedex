package com.uvg.mypokedex.data.model

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonBasic>
)

data class PokemonBasic(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.trimEnd('/').split("/").last().toInt()
}