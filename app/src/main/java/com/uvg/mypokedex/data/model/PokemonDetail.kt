package com.uvg.mypokedex.data.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<StatSlot>
)

data class Sprites(
    val front_default: String?,
    val other: OtherSprites?
)

data class OtherSprites(
    val official_artwork: OfficialArtwork?
)

data class OfficialArtwork(
    val front_default: String?
)

data class TypeSlot(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String,
    val url: String
)

data class StatSlot(
    val base_stat: Int,
    val effort: Int,
    val stat: Stat
)

data class Stat(
    val name: String,
    val url: String
)