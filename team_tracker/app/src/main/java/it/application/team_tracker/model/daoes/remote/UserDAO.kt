package it.application.team_tracker.model.daoes.remote

import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow

interface UserDAO {
    /**
     * return the user if it exists or null otherwise
     */
    fun getUser(userId: String,listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<User?>

    fun getUserLikeNickname(nickname: String,listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<User?>

    fun updateUser(user: User): Flow<Boolean>
    //TODO aggiungere funzioni per kpi
}