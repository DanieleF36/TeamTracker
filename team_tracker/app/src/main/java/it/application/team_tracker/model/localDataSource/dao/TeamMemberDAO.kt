package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.application.team_tracker.model.localDataSource.entities.TeamMember

@Dao
interface TeamMemberDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTeamMember(teamMember: TeamMember)
    @Query("DELETE FROM team_member WHERE team_id=:idTeam AND user_id=:idUser")
    suspend fun deleteTeamMember(idTeam: String, idUser: String)
}