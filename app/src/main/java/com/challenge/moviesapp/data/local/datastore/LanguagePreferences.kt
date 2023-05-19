package com.challenge.moviesapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

@Suppress("unused", "RedundantSuppression")
class LanguagePreferences(private val context: Context) {
    companion object {
        private val Context.counterDataStore by preferencesDataStore(
            name = "LANG_PREFERENCES"
        )
        val langKey = stringPreferencesKey("lang")
    }
    suspend fun setLanguage(language: String) {
        context.counterDataStore.edit { preferences ->
            preferences[langKey] = language
        }
    }

    fun getLanguage(): Flow<String> {
        return context.counterDataStore.data
            .map { preferences ->
                preferences[langKey] ?: Locale.getDefault().language
            }
    }
}