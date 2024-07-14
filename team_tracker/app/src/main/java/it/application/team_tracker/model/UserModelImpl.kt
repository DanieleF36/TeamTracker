package it.application.team_tracker.model

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import it.application.team_tracker.model.localDataSource.dao.UserDAO
import it.application.team_tracker.model.neutralEntities.User
import javax.inject.Inject

class UserModelImpl @Inject constructor(private val dao: UserDAO): UserModel {
    private lateinit var loggedUser: MutableState<User?>

    init {
        //TODO inizializare loggedUser con una richiesta al remoteDataSource
        loggedUser = mutableStateOf(null)
    }

    override fun getLoggedUser(): State<User?> {
        return loggedUser
    }
}