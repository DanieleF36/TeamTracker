package it.application.team_tracker.model.remoteDataSource.daoes.interfaces

import it.application.team_tracker.model.remoteDataSource.entities.User
import kotlinx.coroutines.flow.Flow

interface UserDAO {
    fun getUser(id: String, listenUpdate: Boolean = true): Flow<User?>
    fun getByNickname(nickname: String, listenUpdate: Boolean = true): Flow<User?>
    fun getLikeNickname(nickname: String, listenUpdate: Boolean = true): Flow<User?>
    fun addUser(user: User, listenUpdate: Boolean = true): Flow<Boolean>
    fun addUsers(vararg user: User, listenUpdate: Boolean = true): Flow<Boolean>
    fun updateUser(user: User, listenUpdate: Boolean = true): Flow<Boolean>
    fun deleteUser(id: String, listenUpdate: Boolean = true): Flow<Boolean>
}