package com.github.xs93.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 10:27
 * @description
 *
 */

operator fun <T> Preferences.get(preference: DataStorePreference<T>) = this[preference.key]

open class DataStorePreference<T>(
    private val dataStore: DataStore<Preferences>,
    val key: Preferences.Key<T>,
    open val default: T?
) {

    private suspend fun set(block: suspend T?.(Preferences) -> T?): Preferences =
        dataStore.edit { preferences ->
            val value = block(preferences[key] ?: default, preferences)
            if (value == null) {
                preferences.remove(key)
            } else {
                preferences[key] = value
            }
        }

    suspend fun set(value: T?): Preferences = set { value }

    fun asFlow(): Flow<T?> = dataStore.data.map { it[key] ?: default }

    suspend fun get(): T? = asFlow().first()

    suspend fun getOrDefault(): T = get() ?: throw IllegalStateException("No default value")
}