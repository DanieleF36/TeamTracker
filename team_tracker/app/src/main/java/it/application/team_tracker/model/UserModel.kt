package it.application.team_tracker.model

import androidx.compose.runtime.State
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow

interface UserModel {
    fun getLoggedUser(): State<User?>
    fun getUserLikeNickname(nick: String): Flow<List<User>>
    fun getUser(id: String): Flow<User?>
    suspend fun addUser(user: User)
}