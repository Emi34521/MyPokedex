package com.uvg.mypokedex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonJsonResponse(
    val range: Range,
    val items: List<PokemonJson>
)

@Serializable
data class Range(
    val start: Int,
    val end: Int
)

@Serializable
data class PokemonJson(
    val id: Int,
    val name: String,
    val type: List<String>,
    val weight: Float,
    val height: Float,
    val stats: List<StatJson>
)

@Serializable
data class StatJson(
    val name: String,
    val value: Int
)

fun PokemonJson.toPokemon(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        types = type,
        weight = weight,
        height = height,
        stats = Stats(
            hp = stats.find { it.name == "hp" }?.value ?: 0,
            attack = stats.find { it.name == "attack" }?.value ?: 0,
            defense = stats.find { it.name == "defense" }?.value ?: 0,
            specialAttack = stats.find { it.name == "special-attack" }?.value ?: 0,
            specialDefense = stats.find { it.name == "special-defense" }?.value ?: 0,
            speed = stats.find { it.name == "speed" }?.value ?: 0
        )
    )
}