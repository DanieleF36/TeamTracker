package it.application.team_tracker.model

import android.content.Context
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow

interface UserModel {
    fun login(): Flow<User?>
    /**
     * this function may need the context for drawing google credential manager
     * returns the id if the login gone well
     * throw IllegalStateException with Internal error or No account available for the login
     */
    fun loginGoogle(context: Context?): Flow<User?>
    fun updateLoggedUser(new: User): Flow<Boolean>
    fun getUserLikeNickname(nick: String): Flow<User?>
    fun getUser(id: String): Flow<User?>
    fun setFavoriteTeam(teamId: String, userId: String, add: Boolean): Flow<Boolean>
}