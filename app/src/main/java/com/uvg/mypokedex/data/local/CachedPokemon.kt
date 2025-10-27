package com.uvg.mypokedex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "cached_pokemon")
@TypeConverters(Converters::class)
data class CachedPokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val stats: List<PokemonStat>,
    val lastFetchedAt: Long = System.currentTimeMillis()
)

data class PokemonStat(
    val name: String,
    val value: Int
)

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromStatList(value: List<PokemonStat>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStatList(value: String): List<PokemonStat> {
        val listType = object : TypeToken<List<PokemonStat>>() {}.type
        return gson.fromJson(value, listType)
    }
}
