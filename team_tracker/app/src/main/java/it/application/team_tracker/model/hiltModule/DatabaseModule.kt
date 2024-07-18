package it.application.team_tracker.model.hiltModule

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.application.team_tracker.model.UserModel
import it.application.team_tracker.model.UserModelImpl
import it.application.team_tracker.model.localDataSource.dao.UserDAO
import it.application.team_tracker.model.localDataSource.database.ApplicationDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{
    @Provides
    @Singleton
    fun provideApplicationDB(@ApplicationContext appContext: Context): ApplicationDB{
        return Room.databaseBuilder(
            appContext,
            ApplicationDB::class.java,
            "app_db").build()
    }


    @Provides
    fun provideUserDao(database: ApplicationDB): UserDAO {
        return database.userDao()
    }
    @Provides
    @Singleton
    fun provideUserModel(userDao: UserDAO): UserModel {
        return UserModelImpl(userDao)
    }
}