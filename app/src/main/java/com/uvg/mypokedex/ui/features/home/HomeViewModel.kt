package com.uvg.mypokedex.ui.features.home

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException

class HomeViewModel(private val context: Context): ViewModel() {

    //variable para guardar la lista de pokemons
    val pokemonList = mutableStateListOf<Pokemon>()

    private var currentPage = 0
    private val pageSize = 10

    // Modelos para deserialización JSON
    @Serializable
    private data class PokemonResponse(
        val range: Range,
        val items: List<PokemonJson>
    )

    @Serializable
    private data class Range(val start: Int, val end: Int)

    @Serializable
    private data class PokemonJson(
        val id: Int,
        val name: String,
        @SerialName("type") val types: List<String>, // JSON usa "type"
        val weight: Float,
        val height: Float,
        val stats: List<StatJson>
    )

    @Serializable
    private data class StatJson(val name: String, val value: Int)

    // Construye dinámicamente el nombre del archivo a partir de la página
    private fun getFileNameForPage(page: Int): String {
        // El nombre del archivo sigue un patrón específico
        val start = page * pageSize + 1
        val end = (page + 1) * pageSize
        // "pokemon_001_010.json" es el nombre del archivo para la página 0
        return "pokemon_${start.toString().padStart(3, '0')}_${end.toString().padStart(3, '0')}.json"
    }

    // Leer archivo desde assets
    private fun loadJsonFromAssets(fileName: String): String {
        val inputStream = context.assets.open(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }

    // Función principal para cargar más Pokémon
    fun loadMorePokemon() {
        try {
            val fileName = getFileNameForPage(currentPage)
            val jsonString = loadJsonFromAssets(fileName)

            // Configurar JSON decoder
            val json = Json { ignoreUnknownKeys = true }

            // Deserializar el JSON completo
            val response = json.decodeFromString<PokemonResponse>(jsonString)

            // Convertir a tus modelos
            val newPokemons = response.items.map { pokemonJson ->
                val statsMap = pokemonJson.stats.associate { it.name to it.value }
                Pokemon(
                    id = pokemonJson.id,
                    name = pokemonJson.name,
                    types = pokemonJson.types,
                    weight = pokemonJson.weight,
                    height = pokemonJson.height,
                    stats = Stats(
                        hp = statsMap["hp"] ?: 0,
                        attack = statsMap["attack"] ?: 0,
                        defense = statsMap["defense"] ?: 0,
                        specialAttack = statsMap["special-attack"] ?: 0,
                        specialDefense = statsMap["special-defense"] ?: 0,
                        speed = statsMap["speed"] ?: 0
                    )
                )
            }

            pokemonList.addAll(newPokemons)

            currentPage++  // avanzar a la siguiente página
        } catch (e: Exception) {
            e.printStackTrace()
            // Si no encuentra más archivos, no hace nada
        }
    }

    fun getPokemonList(): List<Pokemon> {
        // Si la lista está vacía, cargar la primera página
        if (pokemonList.isEmpty()) {
            loadMorePokemon()
        }
        return pokemonList
    }
}