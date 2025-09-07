package com.uvg.mypokedex.ui.features.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats
import kotlinx.serialization.json.Json
import org.json.JSONObject

class HomeViewModel(private val context: Context): ViewModel() {

    //variable para guardar la lista de pokemons
    val pokemonList = mutableListOf<Pokemon>()

    private var currentPage = 0
    private val pageSize = 20

    // Construye dinámicamente el nombre del archivo a partir de la página
    private fun getFileNameForPage(page: Int): String {
        // El nombre del archivo sigue un patrón específico
        val start = page * pageSize + 1
        val end = (page + 1) * pageSize
        // "pokemon_001_20.json" es el nombre del archivo para la página 0
        return "pokemon_${start.toString().padStart(3, '0')}_${end}.json"
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

            val newPokemons = Json.decodeFromString<List<Pokemon>>(jsonString)

            pokemonList.addAll(newPokemons)

            currentPage++  // avanzar a la siguiente página
        } catch (e: Exception) {
            e.printStackTrace()
            // Si no encuentra más archivos, no hace nada
        }
    }


    fun getPokemonList(): List<Pokemon> {
        return listOf(
            Pokemon(
                id = 1,
                name = "Bulbasaur",
                types = listOf("Grass", "Poison"),
                weight = 6.9f,
                height = 0.7f,
                stats = Stats(
                    hp = 45,
                    attack = 49,
                    defense = 49,
                    specialAttack = 65,
                    specialDefense = 65,
                    speed = 45
                )
            ),
            Pokemon(
                id = 4,
                name = "Charmander",
                types = listOf("Fire"),
                weight = 8.5f,
                height = 0.6f,
                stats = Stats(
                    hp = 39,
                    attack = 52,
                    defense = 43,
                    specialAttack = 60,
                    specialDefense = 50,
                    speed = 65
                )
            ),
            Pokemon(
                id = 7,
                name = "Squirtle",
                types = listOf("Water"),
                weight = 9.0f,
                height = 0.5f,
                stats = Stats(
                    hp = 44,
                    attack = 48,
                    defense = 65,
                    specialAttack = 50,
                    specialDefense = 64,
                    speed = 43
                )
            ),
            Pokemon(
                id = 150,
                name = "Mewtwo",
                types = listOf("Psychic"),
                weight = 122.0f,
                height = 2.0f,
                stats = Stats(
                    hp = 106,
                    attack = 110,
                    defense = 90,
                    specialAttack = 154,
                    specialDefense = 90,
                    speed = 130
                )
            )
        )
    }
}
