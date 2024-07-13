package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.Team

@Dao
interface TeamDAO {
    @Query("SELECT * FROM team WHERE id=:id")
    suspend fun getTeam(id: String): Team
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTeam(team: Team)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTeams(vararg team: Team)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateTeam(team: Team)
    @Query("DELETE FROM task WHERE id=:id")
    suspend fun deleteTeam(id: String)
}