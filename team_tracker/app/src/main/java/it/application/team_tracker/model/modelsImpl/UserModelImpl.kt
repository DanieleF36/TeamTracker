package it.application.team_tracker.model.modelsImpl

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
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
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
        if(new.id != loggedUser.value!!.id)
            throw IllegalArgumentException("You are trying to update a different user from logged one")
        if(!validateUser(new))
            throw IllegalArgumentException("User is not well defined")

        if(loggedUser.value!!.photo != new.photo){
            if(new.photo != null) {
                return remote.userDao().changePhoto(new.photo, new.id).onCompletion {
                    if(it == null || it is InternetUnavailableException)
                        local.userDao().changePhoto(new.photo, new.id)
                }.flatMapConcat { newUri ->
                    if (newUri != null)
                        updateLoggedUserDoc(new.copy(photo = Uri.parse(newUri)))
                    else
                        flowOf(false)
                }
            }
            else{
                return remote.userDao().deletePhoto(loggedUser.value!!.id).onCompletion {
                    if(it == null || it is InternetUnavailableException)
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
        if(!validateUser(new))
            throw IllegalArgumentException("User is not well defined")
        return remote.userDao().updateUser(loggedUser.value!!, new).onCompletion {
            if(it == null || it is InternetUnavailableException)
                local.userDao().updateUser(loggedUser.value!!, new)
        }
    }

    private fun validateUser(u: User): Boolean{
        if(u.fullName.isBlank())
            return false
        if(u.nickname.isBlank() || u.nickname.length < 4)
            return false
        if(u.email.isBlank() || !u.email.contains("@"))
            return false
        if(u.description.isBlank())
            return false
        if(u.location.isBlank() || u.location.length <3)
            return false
        if(u.phone.isBlank() || u.phone.length <10)
            return false
        return true
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

    override fun getLoggedUser(): User? {
        return _loggedUser.value
    }

    override fun setFavoriteTeam(teamId: String, userId: String, add: Boolean): Flow<Boolean> {
        return remote.userDao().setFavoriteTeam(teamId, userId, add).onCompletion {
            if(it == null || it is InternetUnavailableException)
                local.userDao().setFavoriteTeam(teamId, userId, add)
        }
    }

}