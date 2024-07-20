package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import it.application.team_tracker.model.localDataSource.room.entities.UserWithTeams

@Dao
interface UserWithTeamDAO {
    @Transaction
    @Query("SELECT * FROM user WHERE id=:userId")
    suspend fun getTeamByUser(userId: String): List<UserWithTeams>
}