package it.application.team_tracker.model

import it.application.team_tracker.model.daoes.local.*

interface LocalDataSource {
     fun messageDao(): MessageDAO
     fun taskDao(): TaskDAO
     fun teamDao(): TeamDAO
     fun userDao(): UserDAO
}