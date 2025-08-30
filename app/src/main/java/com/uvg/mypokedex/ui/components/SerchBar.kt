package com.uvg.mypokedex.ui.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonCard

@Composable
fun PokemonSearchBar(modifier: Modifier = Modifier,allPokemons: List<Pokemon>) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var filteredPokemons = allPokemons.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Column{
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(text = "Buscar Pokémon") },
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn {
            items(filteredPokemons) { pokemon ->
                PokemonCard(pokemon = pokemon)
            }
        }

    }
}


