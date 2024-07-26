package it.application.team_tracker.model.userModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import it.application.team_tracker.model.LocalDataSource
import it.application.team_tracker.model.RemoteDataSource
import it.application.team_tracker.model.UserModel
import it.application.team_tracker.model.entities.Team
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserModelImpl @Inject constructor(): UserModel {
    //TODO inizializare loggedUser con una richiesta al remoteDataSource
    private var loggedUser: MutableState<User?> = mutableStateOf(null)

    private val _userCache: MutableStateFlow<MutableList<User>> =
        MutableStateFlow(emptyList<User>().toMutableList())
    val userCache: StateFlow<List<User>> = _userCache
    private val maxSize = 20

    @Inject
    lateinit var local: LocalDataSource
    @Inject
    lateinit var remote: RemoteDataSource

    private fun addUserToCache(user: User){
        _userCache.value.add(user)
        if(_userCache.value.size>maxSize){
            _userCache.value.removeFirst()
        }
    }
    //TODO aggiungere chiamata remota per il login
    override fun getLoggedUser(): State<User?> {
        return loggedUser
    }
    //TODO gestire foto
    override fun updateLoggedUser(new: User): Flow<Boolean> {
        if(loggedUser.value == null)
            throw IllegalStateException("No user is logged")
        return remote.userDao().updateUser(loggedUser.value!!, new).onEach {
            if(it)
                local.userDao().updateUser(loggedUser.value!!, new)
        }
    }

    override fun getUserLikeNickname(nick: String): Flow<User?> {
        return remote.userDao().getUserLikeNickname(nick)
    }
    //TODO gestire foto
    override fun getUser(id: String): Flow<User?> {
        return local.userDao().getUser(id).flatMapLatest { userLocal ->
            if (userLocal != null)
                flowOf(userLocal)
            else {
                remote.userDao().getUser(id).onEach { remoteUser ->
                    if(remoteUser != null){
                        local.userDao().addUser(remoteUser)
                        addUserToCache(remoteUser)
                    }
                }
            }
        }
    }

    override fun setFavoriteTeam(teamId: String, userId: String, add: Boolean): Flow<Boolean> {
        return remote.userDao().setFavoriteTeam(teamId, userId, add).onEach {
            if(it){
                local.userDao().setFavoriteTeam(teamId, userId, add)
            }
        }
    }

}