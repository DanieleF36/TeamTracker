package it.application.team_tracker.model.daoes.remote

import it.application.team_tracker.model.entities.Team
import kotlinx.coroutines.flow.Flow

interface TeamDAO {

    /**
     * returns the teams of which the user is a member or null if it is not a member of any team
     */
    fun getUserTeams(userId: String): Flow<Team?>
    /**
     * returns a pair of change type linked to the team of which the user is a member or null if it is not a member of any team
     */
    fun getUserTeams(userId: String, listenForUpdates: Boolean = true): Flow<Pair<ChangeType, Team>?>

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