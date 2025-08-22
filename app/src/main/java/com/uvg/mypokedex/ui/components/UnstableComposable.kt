package com.uvg.mypokedex.ui.components
///ui/components/UnstableComposable.kt


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlin.random.Random
// PREGUNTAS POR HACERSE:
// 1. ¿No es idempotente?
// 2. ¿Tiene efectos secundarios?
// 3. ¿Usa un tipo de dato inestable (List) como parámetro?
@Composable
fun UnstablePokemonList(pokemons: List<String>) {
    val randomColor = String.format("#%06x", Random.nextInt(0, 0xFFFFFF))
    println("Composing UnstablePokemonList with color $randomColor")
    Button(onClick = { /* no hace nada, solo para forzar recomposición */ }) {
        Text(text = "Tengo ${pokemons.size} Pokémon favoritos")
    }
}