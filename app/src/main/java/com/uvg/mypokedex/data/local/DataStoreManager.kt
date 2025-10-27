package com.uvg.mypokedex.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Crear DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sort_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        private val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        private val DEFAULT_SORT_ORDER = "BY_NUMBER_ASC"
    }

    // Obtener el orden guardado
    val sortOrderFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[SORT_ORDER_KEY] ?: DEFAULT_SORT_ORDER
        }

    // Guardar nuevo orden
    suspend fun saveSortOrder(sortOrder: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder
        }
    }
}