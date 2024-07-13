package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Query
import it.application.team_tracker.model.localDataSource.entities.Team

@Dao
interface TeamCompleteDAO {
    @Query("SELECT * FROM team WHERE id=:idTeam")
    suspend fun getTeam(idTeam: String): Team
}