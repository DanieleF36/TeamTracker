package it.application.team_tracker.model.daoes.local

import android.net.Uri
import it.application.team_tracker.model.entities.Team
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow

interface UserDAO {
    /**
     * return the user if it exists or null otherwise
     */
    fun getUser(userId: String): Flow<User?>

    fun getUserLikeNickname(nickname: String): Flow<User?>

    fun updateUser(old: User, new: User): Flow<Boolean>

    fun changePhoto(uri: Uri, userId: String): Flow<String?>

    fun deletePhoto(userId: String): Flow<Boolean>

    /**
     * add a user and return its id
     */
    fun addUser(user: User):Flow<String>
    /**
     * this function have to be used in both ways, to add or remove a favorite team
     */
    fun setFavoriteTeam(teamId: String, userId: String, add: Boolean): Flow<Boolean>
    /**
     * returns the teams of which the user is a member or null if it is not a member of any team
     */
    fun getTeams(userId: String): Flow<Team?>
    //TODO aggiungere funzioni per i kpi
}