package com.uvg.mypokedex.data.local

import androidx.room.*
import androidx.room.Query
import com.uvg.mypokedex.data.local.entity.CachedPokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM cached_pokemon ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<CachedPokemon>>

    @Query("SELECT * FROM cached_pokemon WHERE id = :pokemonId")
    suspend fun getPokemonById(pokemonId: Int): CachedPokemon?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: CachedPokemon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemons: List<CachedPokemon>)

    @Query("DELETE FROM cached_pokemon")
    suspend fun deleteAllPokemon()

    @Query("SELECT COUNT(*) FROM cached_pokemon")
    suspend fun getPokemonCount(): Int

    @Query("SELECT MAX(lastFetchedAt) FROM cached_pokemon")
    suspend fun getLastFetchTime(): Long?
}