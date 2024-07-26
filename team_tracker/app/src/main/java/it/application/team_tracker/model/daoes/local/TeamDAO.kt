package it.application.team_tracker.model.daoes.local

import it.application.team_tracker.model.entities.Team
import kotlinx.coroutines.flow.Flow

interface TeamDAO {

    fun updateTeam(team: Team): Flow<Boolean>

    fun deleteTeam(team: Team): Flow<Boolean>

    fun updateUserRole(userId: String, newRole: String): Flow<Boolean>

    fun removeUser(teamId: String, userId: String): Flow<Boolean>

    fun generateInvitationCode(teamId: String): Flow<Boolean>
    /**
     * add a team and return its id
     */
    fun addTeam(team: Team): Flow<String>
    /* TODO da decidere quali stats usare
    fun getStats
    */
}