package com.uvg.mypokedex.data.model

import  kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<String>,
    val weight: Float,       // kg
    val height: Float,       // en m
    val stats: Stats         // objeto con los stats
)

@Serializable
data class Stats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
)

// 🔹 Función que recorre stats
fun Stats.toMap(): Map<String, Int> = mapOf(
    "HP" to hp,
    "Attack" to attack,
    "Defense" to defense,
    "Sp. Atk" to specialAttack,
    "Sp. Def" to specialDefense,
    "Speed" to speed
)
