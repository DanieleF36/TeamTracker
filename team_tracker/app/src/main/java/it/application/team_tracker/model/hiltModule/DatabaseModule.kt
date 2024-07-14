package it.application.team_tracker.model.hiltModule

import dagger.Module
import dagger.Provides
import it.application.team_tracker.model.localDataSource.dao.UserDAO
import it.application.team_tracker.model.localDataSource.database.ApplicationDB

@Module
object DatabaseModule{
    @Provides
    fun provideUserDao(database: ApplicationDB): UserDAO {
        return database.userDao()
    }
}