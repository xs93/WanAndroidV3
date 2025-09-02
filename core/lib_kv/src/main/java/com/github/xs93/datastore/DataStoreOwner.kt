package com.github.xs93.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.xs93.kv.KVInitializer
import kotlin.properties.ReadOnlyProperty

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 10:35
 * @description
 *
 */


open class DataStoreOwner(name: String) : IDataStoreOwner {
    private val Context.dataStore by preferencesDataStore(name = name)
    override val dataStore: DataStore<Preferences>
        get() = context.dataStore
}

interface IDataStoreOwner {

    val context: Context get() = KVInitializer.appContext

    val dataStore: DataStore<Preferences>

    fun intPreference(
        key: String,
        default: Int = 0
    ): ReadOnlyProperty<IDataStoreOwner, DataStorePreference<Int>> =
        PreferenceProperty<Int>(key, ::intPreferencesKey, default)

    fun doublePreference(
        key: String,
        default: Double = 0.0
    ): ReadOnlyProperty<IDataStoreOwner, DataStorePreference<Double>> =
        PreferenceProperty(key, ::doublePreferencesKey, default)

    fun longPreference(
        key: String,
        default: Long = 0L
    ): ReadOnlyProperty<IDataStoreOwner, DataStorePreference<Long>> =
        PreferenceProperty(key, ::longPreferencesKey, default)

    fun floatPreference(
        key: String,
        default: Float = 0.0f
    ): ReadOnlyProperty<IDataStoreOwner, DataStorePreference<Float>> =
        PreferenceProperty(key, ::floatPreferencesKey, default)

    fun booleanPreference(
        key: String,
        default: Boolean = false
    ): ReadOnlyProperty<IDataStoreOwner, DataStorePreference<Boolean>> =
        PreferenceProperty(key, ::booleanPreferencesKey, default)

    fun stringPreference(
        key: String,
        default: String? = null
    ): ReadOnlyProperty<IDataStoreOwner, DataStorePreference<String>> =
        PreferenceProperty(key, ::stringPreferencesKey, default)

    fun stringSetPreference(
        key: String,
        default: Set<String>? = null
    ): ReadOnlyProperty<IDataStoreOwner, DataStorePreference<Set<String>>> =
        PreferenceProperty(key, ::stringSetPreferencesKey, default)

    fun byteArrayPreference(
        key: String,
        default: ByteArray? = null
    ): ReadOnlyProperty<IDataStoreOwner, DataStorePreference<ByteArray>> =
        PreferenceProperty(key, ::byteArrayPreferencesKey, default)

    class PreferenceProperty<T>(
        private val keyName: String,
        private val key: (String) -> Preferences.Key<T>,
        private val default: T? = null
    ) : ReadOnlyProperty<IDataStoreOwner, DataStorePreference<T>> {
        private var cache: DataStorePreference<T>? = null

        override fun getValue(
            thisRef: IDataStoreOwner,
            property: kotlin.reflect.KProperty<*>
        ): DataStorePreference<T> =
            cache ?: DataStorePreference(thisRef.dataStore, key(keyName), default).also {
                cache = it
            }
    }
}