package it.application.team_tracker.model.daoes.remote

import android.net.Uri
import it.application.team_tracker.model.entities.Team
import kotlinx.coroutines.flow.Flow

interface TeamDAO {

    fun updateTeam(oldTeam: Team, newTeam: Team): Flow<Boolean>

    fun deleteTeam(teamId: String): Flow<Boolean>

    fun updateUserRole(teamId: String, userId: String, newRole: String): Flow<Boolean>

    fun changePhoto(uri: Uri, teamId: String): Flow<String?>

    fun deletePhoto(teamId: String): Flow<Boolean>

    fun removeUser(teamId: String, userId: String): Flow<Boolean>

    fun addUser(teamId: String, userId: String, role: String): Flow<Boolean>

    fun generateInvitationCode(teamId: String): Flow<String?>

    /**
     * add a team and return its id
     */
    fun addTeam(team: Team): Flow<String?>
    /* TODO da decidere quali stats usare
    fun getStats
    */
}