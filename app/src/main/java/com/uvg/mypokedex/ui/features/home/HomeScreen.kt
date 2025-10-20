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
import com.uvg.mypokedex.ui.components.PokemonCardClickable

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    // Brindar contexto a ViewModel
    viewModel: HomeViewModel = HomeViewModel(LocalContext.current.applicationContext as Application),
    onPokemonClick: (Int) -> Unit = {},
    onSearchToolsClick: () -> Unit = {}
) {
    val pokemonList = viewModel.getPokemons()
    if (pokemonList.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading Pokémon or no Pokémon found...")

        }
        return // Exit early if the list is empty
    }
    val state = rememberLazyGridState()

    LaunchedEffect(state) { // a cada scroll crea snapshot
        snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            // averigua index del ultimo elemento visible
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex == pokemonList.lastIndex) {
                    // recoge el index y lo compara con el de la lista
                    viewModel.loadMorePokemon()
                }
            }
    }
    LazyVerticalGrid(
        state = state,
        //siempre que se corre el codigo, recomposicione, se mostrara el ultimo estado
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        //espaciado del objeto
        verticalArrangement = Arrangement.SpaceBetween,
        //espaciado entre elementos
        columns = GridCells.Fixed(2)
        //cantidad de columnas
    ) {
        items(pokemonList, key = { it.id }) { pokemon: Pokemon ->
            //los elementos del grid son las cartas
            PokemonCardClickable(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) }
            )
        }
    }
}