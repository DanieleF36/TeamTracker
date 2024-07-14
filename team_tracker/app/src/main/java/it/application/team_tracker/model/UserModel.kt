package it.application.team_tracker.model

import androidx.compose.runtime.State
import it.application.team_tracker.model.neutralEntities.User

interface UserModel {
    fun getLoggedUser(): State<User?>
}