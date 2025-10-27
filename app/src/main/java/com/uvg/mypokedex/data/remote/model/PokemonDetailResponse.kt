package com.uvg.mypokedex.data.remote.model

import com.google.gson.annotations.SerializedName

// Modelo de datos para la respuesta de la API de Pokémon
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonType>,
    val sprites: PokemonSprites
)

data class PokemonType(
    val slot: Int,
    val type: TypeInfo
)

data class TypeInfo(
    val name: String,
    val url: String
)

data class PokemonSprites(
    // ? indica que puede ser nulo
    val frontDefault: String?,
    val other: OtherSprites?
)

data class OtherSprites(
    // serialized to JSON with the provided name value.
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    val frontDefault: String?
)