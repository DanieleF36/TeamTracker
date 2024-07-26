package it.application.team_tracker.model

import androidx.compose.runtime.State
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow

interface UserModel {
    fun getLoggedUser(): State<User?>
    fun updateLoggedUser(new: User): Flow<Boolean>
    fun getUserLikeNickname(nick: String): Flow<User?>
    fun getUser(id: String): Flow<User?>
    fun setFavoriteTeam(teamId: String, userId: String, add: Boolean): Flow<Boolean>
}