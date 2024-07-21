package it.application.team_tracker.model.userModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import it.application.team_tracker.model.LocalDataSource
import it.application.team_tracker.model.UserModel
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class UserModelImpl: UserModel {
    //TODO inizializare loggedUser con una richiesta al remoteDataSource
    private var loggedUser: MutableState<User?> = mutableStateOf(null)

    private val _userCache: MutableStateFlow<MutableList<User>> =
        MutableStateFlow(emptyList<User>().toMutableList())
    val userCache: StateFlow<List<User>> = _userCache
    private val maxSize = 20
    //TODO devono essere dei DAO
    private lateinit var local: LocalDataSource
    private lateinit var remote: LocalDataSource

    private fun addUserToCache(user: User){
        _userCache.value.add(user)
        if(_userCache.value.size>maxSize){
            _userCache.value.removeFirst()
        }
    }

    override fun getLoggedUser(): State<User?> {
        return loggedUser
    }

    override fun getUserLikeNickname(nick: String): Flow<User?> = flow {
        withContext(Dispatchers.IO) {
            local.userDao().getUserLikeNickname(nick).flatMapConcat { localUser ->
                if (localUser != null)
                    flowOf(localUser)
                else
                    remote.userDao().getUserLikeNickname(nick)
            }.collect {
                emit(it)
                if (it != null)
                    addUserToCache(it)
            }
        }
    }

    override fun getUser(id: String): Flow<User?> = flow {
        withContext(Dispatchers.IO) {
            local.userDao().getUser(id).collect {
                if (it != null)
                    emit((it))
                else {
                    val u = remote.userDao().getUser(id).first()
                    emit(u)
                    if (u != null)
                        local.userDao().addUser(u)
                }
            }
        }
    }

    override suspend fun addUser(user: User) {
        TODO()
    }
}

private fun fromUserToNeutralUser(user: it.application.team_tracker.model.localDataSource.room.entities.User): User {
    return User(
        id= user.id,
        nickname = user.nickname,
        fullName = user.fullName,
        email = user.email,
        location = user.location,
        phone = user.phone,
        description = user.description,
        photo = user.photo,
        teamsId = null,
        tasksId = null,
        privateChatsId = null
    )
}

private fun fromNeutralUserToUser(user: User): it.application.team_tracker.model.localDataSource.room.entities.User{
    return it.application.team_tracker.model.localDataSource.room.entities.User(
        id= user.id,
        nickname = user.nickname,
        fullName = user.fullName,
        email = user.email,
        location = user.location,
        phone = user.phone,
        description = user.description,
        photo = user.photo
    )
}