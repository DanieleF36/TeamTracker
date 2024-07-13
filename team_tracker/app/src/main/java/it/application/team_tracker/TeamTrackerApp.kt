package it.application.team_tracker

import android.app.Application
import androidx.room.Room
import it.application.team_tracker.model.localDataSource.database.ApplicationDB

class TeamTrackerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        TeamTrackerApp.db = Room.databaseBuilder(
            this.applicationContext,
            ApplicationDB::class.java,
            "app_db").build()
    }

    companion object {
        lateinit var db: ApplicationDB
    }
}