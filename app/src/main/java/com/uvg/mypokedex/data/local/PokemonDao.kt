package com.uvg.mypokedex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM cached_pokemon ORDER BY id ASC")
    fun getAllPokemonByNumberAsc(): Flow<List<CachedPokemon>>

    @Query("SELECT * FROM cached_pokemon ORDER BY id DESC")
    fun getAllPokemonByNumberDesc(): Flow<List<CachedPokemon>>

    @Query("SELECT * FROM cached_pokemon ORDER BY name ASC")
    fun getAllPokemonByNameAsc(): Flow<List<CachedPokemon>>

    @Query("SELECT * FROM cached_pokemon ORDER BY name DESC")
    fun getAllPokemonByNameDesc(): Flow<List<CachedPokemon>>

    @Query("SELECT * FROM cached_pokemon WHERE id = :pokemonId")
    fun getPokemonById(pokemonId: Int): Flow<CachedPokemon?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: CachedPokemon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemon: List<CachedPokemon>)

    @Query("DELETE FROM cached_pokemon")
    suspend fun deleteAllPokemon()

    @Query("SELECT COUNT(*) FROM cached_pokemon")
    suspend fun getPokemonCount(): Int

    @Query("SELECT MAX(lastFetchedAt) FROM cached_pokemon")
    suspend fun getLastFetchTime(): Long?
}
