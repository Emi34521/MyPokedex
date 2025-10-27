package com.uvg.mypokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.uvg.mypokedex.data.local.Converters

@Entity(tableName = "cached_pokemon")
@TypeConverters(Converters::class)
data class CachedPokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val weight: Float,
    val height: Float,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
    val lastFetchedAt: Long = System.currentTimeMillis()
)