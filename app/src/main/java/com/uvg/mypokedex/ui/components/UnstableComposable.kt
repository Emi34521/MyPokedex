package com.uvg.mypokedex.ui.components
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random
// PREGUNTAS POR HACERSE:
// 1. ¿No es idempotente?
// 2. ¿Tiene efectos secundarios?
// 3. ¿Usa un tipo de dato inestable (List) como parámetro?


//en esta parte se utilizó IA para generar pedazos del código
/*
preguntas ¿Por qué la terminal no muestra los resultados del log?
¿Por qué el botón no cambia de color?
¿Por qué al hacer click en el logcat no se muestra ningún cambio?
Implementar UnstablePokemonList en MainActivity.kt
¿si utilizo randomColor by remember, este valor siempre será recordado?
¿Cómo convierto una lista mutable a inmutable?

*/
//probablemente toda esta sección tenga que ser comentada antes de agregar las otras implementaciones

@Composable
fun UnstablePokemonList(pokemons: List<String>) {
    // 1. Idempotencia
    val randomColor by remember {
        mutableStateOf(String.format("#%06x", Random.nextInt(0, 0xFFFFFF)))

    }

    // 2. SideEffect para logs controlados
    SideEffect {
        println("StablePokemonList composed with color $randomColor")
    }

    val stablePokemons: ImmutableList<String> = pokemons.toImmutableList()

    Button(onClick = { /* aquí ya no importa recomposición extra */ }) {
        Text(text = "Tengo ${stablePokemons.size} Pokémon favoritos")
    }
}