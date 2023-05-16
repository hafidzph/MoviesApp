package com.challenge.moviesapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class UserPreferences(private val context: Context) {
    companion object {
        private val Context.counterDataStore by preferencesDataStore(
            name = "user_prefs"
        )
        val userIdKey = intPreferencesKey("user_id")
        val usernameKey = stringPreferencesKey("username")
    }

    suspend fun getUserId(): Int? {
        val preferences = context.counterDataStore.data.first()
        return preferences[userIdKey]
    }

    suspend fun getUsername(): String? {
        val preferences = context.counterDataStore.data.first()
        return preferences[usernameKey]
    }


    suspend fun saveId(userId: Int) {
        context.counterDataStore.edit { preferences ->
            preferences[userIdKey] = userId
        }
    }

    suspend fun saveUsername(username: String) {
        context.counterDataStore.edit { preferences ->
            preferences[usernameKey] = username
        }
    }


    suspend fun clearUserIdAndUsername() {
        context.counterDataStore.edit { preferences ->
            preferences.remove(userIdKey)
            preferences.remove(usernameKey)
        }
    }
}