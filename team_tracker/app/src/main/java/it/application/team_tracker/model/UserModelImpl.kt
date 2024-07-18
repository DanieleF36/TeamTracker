package it.application.team_tracker.model

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import it.application.team_tracker.model.localDataSource.dao.UserDAO
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class UserModelImpl @Inject constructor(private val dao: UserDAO): UserModel {
    //TODO inizializare loggedUser con una richiesta al remoteDataSource
    private var loggedUser: MutableState<User?> = mutableStateOf(null)

    private val _userCache: MutableStateFlow<MutableList<User>> = MutableStateFlow(emptyList<User>().toMutableList())
    val userCache: StateFlow<List<User>> = _userCache
    private val maxSize = 20

    override fun getLoggedUser(): State<User?> {
        return loggedUser
    }

    override fun getUserLikeNickname(nick: String): Flow<List<User>> {
        return dao.getLikeNickname(nick).map { value ->
            value.map { fromUserToNeutralUser(it) }
        }
    }

    override fun getUser(id: String): Flow<User?> {
        return flow {
            val user = _userCache.value.find { it.id == id }
            if(user != null)
                emit(user)
            else{
                dao.getUser(id).collect{
                    if(it != null){
                        val u = fromUserToNeutralUser(it)
                        _userCache.update { list ->
                            if (list.size + 1 > maxSize) {
                                list.subList(1, list.size)
                            }
                            list.add(u)
                            list
                        }
                        emit(u)
                    }
                    else emit(null)
                }
            }
        }
    }

    override suspend fun addUser(user: User) {
        try {
            dao.addUser(fromNeutralUserToUser(user))
        }catch (_: SQLiteConstraintException){
            Log.d("ERROR", "SQLite Constraint ")
        }


    }
}

private fun fromUserToNeutralUser(user: it.application.team_tracker.model.localDataSource.entities.User): User {
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

private fun fromNeutralUserToUser(user: User): it.application.team_tracker.model.localDataSource.entities.User{
    return it.application.team_tracker.model.localDataSource.entities.User(
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