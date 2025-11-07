package com.uvg.mypokedex

import android.app.Application
import com.uvg.mypokedex.data.local.PokemonDatabase
import com.uvg.mypokedex.data.network.RetrofitInstance
import com.uvg.mypokedex.data.preferences.UserPreferencesRepository
import com.uvg.mypokedex.data.repository.PokemonRepository
import com.uvg.mypokedex.util.ConnectivityObserver
import com.uvg.mypokedex.util.NetworkConnectivityObserver

class PokemonApplication : Application() {

    lateinit var pokemonRepository: PokemonRepository
        private set

    lateinit var connectivityObserver: ConnectivityObserver
        private set

    override fun onCreate() {
        super.onCreate()

        // Inicializar la base de datos
        val database = PokemonDatabase.getDatabase(this)

        // Inicializar el servicio de API
        val apiService = RetrofitInstance.api

        // Inicializar el repositorio de preferencias
        val preferencesRepository = UserPreferencesRepository(this)

        // Inicializar el observador de conectividad
        connectivityObserver = NetworkConnectivityObserver(this)

        // Inicializar el repositorio principal
        pokemonRepository = PokemonRepository(
            pokemonDao = database.pokemonDao(),
            apiService = apiService,
            preferencesRepository = preferencesRepository,
            connectivityObserver = connectivityObserver
        )
    }
}