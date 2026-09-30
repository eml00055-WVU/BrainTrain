package com.braintrain.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "braintrain_prefs")
private val ACTIVE_USERNAME_KEY = stringPreferencesKey("active_username")

/**
 * Remembers which local profile is "signed in" across app launches. This is
 * intentionally just a device-local pointer Swapping this out for real cloud
 * authentication later should only require replacing this
 * file and PlayerRepository's sign-in logic.
 */
class CurrentUserStore(private val context: Context) {

    val activeUsername: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[ACTIVE_USERNAME_KEY] }

    suspend fun setActiveUsername(username: String?) {
        context.dataStore.edit { prefs ->
            if (username == null) {
                prefs.remove(ACTIVE_USERNAME_KEY)
            } else {
                prefs[ACTIVE_USERNAME_KEY] = username
            }
        }
    }
}
