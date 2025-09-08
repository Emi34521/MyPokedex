package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonCard
import androidx.compose.runtime.derivedStateOf

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    //Inyección de dependencias
    viewModel: HomeViewModel = HomeViewModel(LocalContext.current.applicationContext)
) {
    val pokemonList = viewModel.getPokemonList()
    val state = rememberLazyGridState()

    // Detectar cuando carga mas datos
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = state.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = state.layoutInfo.totalItemsCount

            lastVisibleItem?.let { item ->
                item.index >= totalItems - 3
            } ?: false
        }
    }

//Prompt: Como crear un efecto para añadir más datos
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && pokemonList.isNotEmpty()) {
            viewModel.loadMorePokemon()
        }
    }

    // guardar el ultimo estado de la lista

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
        items(pokemonList) { pokemon: Pokemon ->
            //los elementos del grid son las cartas
            PokemonCard(pokemon)
        }
    }
}