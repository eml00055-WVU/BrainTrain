package com.braintrain.app.data

import android.content.Context
import com.braintrain.app.model.Screen
import kotlinx.coroutines.flow.Flow

/**
 * Single entry point the UI talks to for local-profile accounts and high
 * scores. Wraps Room (persisted player records) and DataStore (which
 * profile is currently active).
 */
class PlayerRepository(context: Context) {

    private val dao = AppDatabase.get(context).playerDao()
    private val currentUserStore = CurrentUserStore(context)

    /** Currently signed-in local profile, or null if nobody has signed in yet. */
    val activeUsername: Flow<String?> = currentUserStore.activeUsername

    /** Every local profile that has ever signed in on this device, for the leaderboard. */
    val allPlayers: Flow<List<PlayerEntity>> = dao.observeAll()

    /**
     * Signs in as [username], creating a new local profile the first time
     * this name is used. Usernames are matched case-sensitively as typed.
     */
    suspend fun signIn(username: String) {
        val trimmed = username.trim()
        if (trimmed.isEmpty()) return
        if (dao.getByUsername(trimmed) == null) {
            dao.insert(PlayerEntity(username = trimmed))
        }
        currentUserStore.setActiveUsername(trimmed)
    }

    suspend fun signOut() {
        currentUserStore.setActiveUsername(null)
    }

    /**
     * Records the score from a just-finished round for [username]/[game],
     * updating that player's personal best only if [score] beats it.
     */
    suspend fun recordScore(username: String, game: Screen, score: Int) {
        val existingRow = dao.getByUsername(username)
        val base = existingRow ?: PlayerEntity(username = username)
        val updated = when (game) {
            Screen.SHAPES -> if (score > base.shapesBest) base.copy(shapesBest = score) else null
            Screen.EQUATIONS -> if (score > base.equationsBest) base.copy(equationsBest = score) else null
            Screen.SYNONYMS -> if (score > base.synonymsBest) base.copy(synonymsBest = score) else null
            else -> null
        }
        when {
            updated != null && existingRow == null -> dao.insert(updated)
            updated != null -> dao.update(updated)
            existingRow == null -> dao.insert(base)
        }
    }
}
