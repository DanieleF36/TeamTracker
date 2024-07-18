package it.application.team_tracker

import android.app.Application
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp
import it.application.team_tracker.model.localDataSource.database.ApplicationDB
@HiltAndroidApp
class TeamTrackerApp: Application()