package com.uvg.mypokedex.ui.features.home

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    // Brindar contexto a ViewModel
    viewModel: HomeViewModel = HomeViewModel(LocalContext.current.applicationContext as Application)
) {
    val pokemonList = viewModel.getPokemons()
    if (pokemonList.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading Pokémons or no Pokémons found...")

        }
        return // Salir en caso de lista vacia
    }
    val state = rememberLazyGridState()

    LaunchedEffect(state) { // a cada scroll crea snapshot
        snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            // averigua index del ultimo elemento visible
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex == pokemonList.lastIndex) {
                    // recoge el index y lo compara con el ultimo de la lista
                    viewModel.loadMorePokemon()
                }
            }
    }
    LazyVerticalGrid(
        state = state,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        columns = GridCells.Fixed(2)
    ) {
        items(pokemonList, key = { it.id }) { pokemon: Pokemon ->
            PokemonCard(pokemon)
        }
    }
}