package com.braintrain.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One row per local device profile. Each game's personal-best score lives
 * as its own column since each mini-game only ever tracks a single "best
 * score", not a history of attempts.
 */
@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val username: String,
    val shapesBest: Int = 0,
    val equationsBest: Int = 0,
    val synonymsBest: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)
