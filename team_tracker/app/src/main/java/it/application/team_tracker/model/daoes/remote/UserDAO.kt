package it.application.team_tracker.model.daoes.remote

import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow

interface UserDAO {
    /**
     * return the user if it exists or null otherwise
     */
    fun getUser(userId: String): Flow<User?>
    /**
     * return a pair of change type linked to the user if it exists or null otherwise
     */
    fun getUserWithUpdate(userId: String): Flow<Pair<ChangeType, User>?>

    fun getUserLikeNickname(nickname: String): Flow<User?>

    fun getUserLikeNicknameWithUpdate(nickname: String): Flow<Pair<ChangeType, User>?>

    fun updateUser(oldUser: User, newUser: User): Flow<Boolean>
    /**
     * this function have to be used in both ways, to add or remove a favorite team
     */
    fun setFavoriteTeam(teamId: String, userId: String, add: Boolean): Flow<Boolean>
    //TODO aggiungere funzioni per kpi
}