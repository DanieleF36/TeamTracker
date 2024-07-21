package it.application.team_tracker.model.daoes.local

import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow

interface UserDAO {
    /**
     * return the user if it exists or null otherwise
     */
    fun getUser(userId: String,listenForUpdates: Boolean = true): Flow<User?>

    fun getUserLikeNickname(nickname: String,listenForUpdates: Boolean = true): Flow<User?>

    fun updateUser(user: User): Flow<Boolean>
    /**
     * add a user and return its id
     */
    fun addUser(user: User):Flow<String>
}