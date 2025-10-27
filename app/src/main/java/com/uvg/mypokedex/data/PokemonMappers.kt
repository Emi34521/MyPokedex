package com.uvg.mypokedex.data

import com.uvg.mypokedex.data.local.entity.CachedPokemon
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats
import com.uvg.mypokedex.data.remote.model.PokemonDetailResponse

/**
 * Convierte un Pokemon del modelo de dominio a CachedPokemon (Room entity)
 */
fun Pokemon.toCachedPokemon(): CachedPokemon {
    return CachedPokemon(
        id = id,
        name = name,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
        types = types,
        weight = weight,
        height = height,
        hp = stats.hp,
        attack = stats.attack,
        defense = stats.defense,
        specialAttack = stats.specialAttack,
        specialDefense = stats.specialDefense,
        speed = stats.speed,
        lastFetchedAt = System.currentTimeMillis()
    )
}

/**
 * Convierte un CachedPokemon (Room entity) a Pokemon del modelo de dominio
 */
fun CachedPokemon.toPokemon(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        types = types,
        weight = weight,
        height = height,
        stats = Stats(
            hp = hp,
            attack = attack,
            defense = defense,
            specialAttack = specialAttack,
            specialDefense = specialDefense,
            speed = speed
        )
    )
}

/**
 * Convierte la respuesta de la API a CachedPokemon
 */
fun PokemonDetailResponse.toCachedPokemon(): CachedPokemon {
    return CachedPokemon(
        id = id,
        name = name,
        imageUrl = sprites.other?.officialArtwork?.front_default
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
        types = types.map { it.type.name },
        weight = weight / 10f, // API devuelve en hectogramos
        height = height / 10f, // API devuelve en decímetros
        hp = 0, // Tendrías que extraer estos de la respuesta completa
        attack = 0,
        defense = 0,
        specialAttack = 0,
        specialDefense = 0,
        speed = 0,
        lastFetchedAt = System.currentTimeMillis()
    )
}

/**
 * Convierte una lista de Pokemon a lista de CachedPokemon
 */
fun List<Pokemon>.toCachedPokemonList(): List<CachedPokemon> {
    return map { it.toCachedPokemon() }
}

/**
 * Convierte una lista de CachedPokemon a lista de Pokemon
 */
fun List<CachedPokemon>.toPokemonList(): List<Pokemon> {
    return map { it.toPokemon() }
}