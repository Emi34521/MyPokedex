package com.uvg.mypokedex.data.remote

import com.uvg.mypokedex.data.remote.api.PokeApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Configuración de Retrofit para la API de Pokémon
object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // ver el cuerpo de las respuestas del log
        level = HttpLoggingInterceptor.Level.BODY
    }

    // necesario para enviar y recibir httprequests
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // adapts a Java interface to HTTP calls
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val pokeApiService: PokeApiService by lazy {
        //Create an implementation of the API endpoints defined by the service interface.
        retrofit.create(PokeApiService::class.java)
    }
}