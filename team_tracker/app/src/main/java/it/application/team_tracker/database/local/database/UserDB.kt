package it.application.team_tracker.database.local.database

import androidx.room.Database
import it.application.team_tracker.database.local.entities.User
import it.application.team_tracker.database.local.dao.UserDAO

@Database(entities = [User::class], version = 1)
abstract class UserDB {
    abstract fun dao(): UserDAO
}