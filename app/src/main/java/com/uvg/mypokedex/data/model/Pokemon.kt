package com.uvg.mypokedex.data.model

data class Pokemon(
    val id: Int,
    val hpMax: Int, //max health-points
    var hp: Int,
    val defInit: Int, //initial defense
    var def: Int,
    val speedInit: Int,
    var speed: Int,
    val accuInit: Int, //initial accuracy
    var accu: Int,
    val evaInit: Int, //initial evasion
    var eva: Int,
    val name: String,
    val type: String
)
