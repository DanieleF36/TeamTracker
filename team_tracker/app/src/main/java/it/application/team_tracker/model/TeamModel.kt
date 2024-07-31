package it.application.team_tracker.model

import it.application.team_tracker.model.entities.Team
import kotlinx.coroutines.flow.Flow

interface TeamModel {
    //TODO se lo faccio restituire User? i cambiamenti allo stato poi si riflettono?
    fun getUserTeam(userId: String, filter: SearchFilter): List<Team?>
    fun addTeam(new: Team): Flow<String?>
    fun updateTeam(oldTeam: Team, newTeam: Team): Flow<Boolean>
    fun deleteTeam(teamId: String): Flow<Boolean>
    fun generateInvitationCode(teamId: String): Flow<String?>
    fun updateUserRole(teamId: String, userId: String, newRole: String): Flow<Boolean>
    //TODO Posso tenere o si fa tutto con update?
    fun removeUser(teamId: String, userId: String): Flow<Boolean>
    fun addUser(teamId: String, userId: String, role: String): Flow<Boolean>
}