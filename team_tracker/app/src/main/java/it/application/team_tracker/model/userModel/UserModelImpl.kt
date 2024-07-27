package it.application.team_tracker.model.userModel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import it.application.team_tracker.model.LocalDataSource
import it.application.team_tracker.model.RemoteDataSource
import it.application.team_tracker.model.UserModel
import it.application.team_tracker.model.entities.User
import it.application.team_tracker.model.exception.InternetUnavailableException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UserModelImpl @Inject constructor(): UserModel {
    private var _loggedUser: MutableState<User?> = mutableStateOf(null)
    val loggedUser: State<User?> = _loggedUser

    private val _userCache: MutableStateFlow<MutableList<User>> = MutableStateFlow(emptyList<User>().toMutableList())
    val userCache: StateFlow<List<User>> = _userCache
    private val maxSize = 20

    @Inject
    lateinit var local: LocalDataSource
    //assumption: in the absence of internet the RemoteDataSource caches the queries and will do them as soon as the internet is back
    @Inject
    lateinit var remote: RemoteDataSource

    private fun addUserToCache(user: User){
        _userCache.value.add(user)
        if(_userCache.value.size>maxSize){
            _userCache.value.removeFirst()
        }
    }

    override fun login(): Flow<User?> {
        return remote.userDao().login().flatMapConcat { userId ->
            if(userId != null) {
                getUser(userId).onEach { _loggedUser.value = it }
                    .catch { e->
                        throw e
                    }
            }
            else
                flowOf(null)
        }.catch { e->
            throw e
        }
    }

    override fun loginGoogle(context: Context?): Flow<User?> {
        return remote.userDao().loginGoogle(context).flatMapConcat { userId ->
            if(userId != null)
                getUser(userId).onEach { _loggedUser.value = it }
                    .catch { e-> throw e }
            else
                flowOf(null)
        }.catch { e-> throw e }
    }

    override fun updateLoggedUser(new: User): Flow<Boolean> {
        if(loggedUser.value == null)
            throw IllegalStateException("No user is logged")
        if(loggedUser.value!!.photo != new.photo){
            if(new.photo != null) {
                return remote.userDao().changePhoto(new.photo, new.id).onEach {
                    if(it != null)
                        local.userDao().changePhoto(new.photo, new.id)
                }.flatMapConcat { newUri ->
                    if (newUri != null)
                        updateLoggedUserDoc(new.copy(photo = Uri.parse(newUri)))
                    else
                        flowOf(false)
                }
            }
            else{
                return remote.userDao().deletePhoto(loggedUser.value!!.id).onEach {
                    if(it)
                        local.userDao().deletePhoto(new.id)
                }.flatMapConcat { res ->
                    if(res)
                        updateLoggedUserDoc(new.copy(photo = null))
                    else
                        flowOf(false)
                }
            }
        }
        return updateLoggedUserDoc(new)
    }

    private fun updateLoggedUserDoc(new: User): Flow<Boolean> {
        return remote.userDao().updateUser(loggedUser.value!!, new).onEach {
            if(it)
                local.userDao().updateUser(loggedUser.value!!, new)
        }
    }

    override fun getUserLikeNickname(nick: String): Flow<User?> {
        return remote.userDao().getUserLikeNickname(nick).catch { e->
            if(e is InternetUnavailableException)
                local.userDao().getUserLikeNickname(nick)
            else
                throw e
        }
    }

    override fun getUser(id: String): Flow<User?> {
        val ret = userCache.value.find { it.id == id }
        if(ret != null)
            return flowOf(ret)
        return local.userDao().getUser(id).flatMapConcat { userLocal ->
            if (userLocal != null)
                flowOf(userLocal)
            else {
                remote.userDao().getUser(id).onEach { remoteUser ->
                    if(remoteUser != null){
                        local.userDao().addUser(remoteUser)
                        addUserToCache(remoteUser)
                    }
                }.catch { e->
                    if(e is InternetUnavailableException)
                        flowOf(null)
                    else
                        throw e
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