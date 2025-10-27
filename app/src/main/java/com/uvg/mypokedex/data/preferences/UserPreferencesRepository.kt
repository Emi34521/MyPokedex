package com.uvg.mypokedex.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

enum class SortOrder {
    NUMBER_ASC,
    NUMBER_DESC,
    NAME_ASC,
    NAME_DESC
}

class UserPreferencesRepository(private val context: Context) {

    private val SORT_ORDER_KEY = stringPreferencesKey("sort_order")

    val sortOrderFlow: Flow<SortOrder> = context.dataStore.data
        .map { preferences ->
            val orderString = preferences[SORT_ORDER_KEY] ?: SortOrder.NUMBER_ASC.name
            try {
                SortOrder.valueOf(orderString)
            } catch (e: IllegalArgumentException) {
                SortOrder.NUMBER_ASC
            }
        }

    suspend fun saveSortOrder(sortOrder: SortOrder) {
        context.dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder.name
        }
    }
}