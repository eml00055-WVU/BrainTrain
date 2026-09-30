package com.braintrain.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Basic selection and insertion queries
 */
@Dao
interface PlayerDao {

    @Query("SELECT * FROM players ORDER BY username COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE username = :username LIMIT 1")
    suspend fun getByUsername(username: String): PlayerEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: PlayerEntity)

    @Update
    suspend fun update(player: PlayerEntity)
}
