package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserWithTeamDAO {
    @Query("SELECT * FROM user WHERE id=:userId")
    suspend fun getTeamByUser(userId: String)
}