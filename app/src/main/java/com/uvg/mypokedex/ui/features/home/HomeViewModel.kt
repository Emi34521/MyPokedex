package com.uvg.mypokedex.ui.features.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.uvg.mypokedex.data.model.Pokemon
import kotlinx.serialization.json.Json

// application, en vez de activity, para obtener contexto
// sino se puede morir el activity, pero el viewmodel no, creando memory leak
class HomeViewModel (application: Application) : AndroidViewModel(application) {

    //variable para guardar la lista de pokemons
    val pokemonList = mutableListOf<Pokemon>()

    private var currentPage = 0
    private val pageSize = 10

    init {
        loadMorePokemon()
    }

    // Construye dinámicamente el nombre del archivo a partir de la página
    private fun getFileNameForPage(page: Int): String {
        val start = page * pageSize + 1
        val end = (page + 1) * pageSize
        // "pokemon_001_10.json" es el nombre del archivo para la página 0
        return "pokemon_${start.toString().padStart(3, '0')}_${end}.json"
        // padStart rellena con 0 lo ingresado hasta llegar a 3 caracteres
    }
    // Leer archivo desde assets
    // assets es un folder para guardar archivos que son usados por la app
    private fun loadJsonFromAssets(fileName: String): String {
        val inputStream = getApplication<Application>().assets.open(fileName)
        return inputStream.bufferedReader().use { it.readText() }
        // .use para cerrar operacion al terminarse de ejecutar
    }
    // leer lista de pokemons del archivo json y agregar a lista
    fun loadMorePokemon() {
        try {
            val fileName = getFileNameForPage(currentPage)
            val jsonString = loadJsonFromAssets(fileName)

            val newPokemons = Json.decodeFromString<List<Pokemon>>(jsonString)

            pokemonList.addAll(newPokemons)

            currentPage++  // avanzar página
        } catch (e: Exception) {
            e.printStackTrace()
            // Si no encuentra más archivos, no hace nada
        }
    }

    fun getPokemons(): MutableList<Pokemon> {
        return pokemonList
    }
}