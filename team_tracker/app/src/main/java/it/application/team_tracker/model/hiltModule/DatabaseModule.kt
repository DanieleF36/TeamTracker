package it.application.team_tracker.model.hiltModule

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.application.team_tracker.model.LocalDataSource
import it.application.team_tracker.model.RemoteDataSource
import it.application.team_tracker.model.UserModel
import it.application.team_tracker.model.localDataSource.LocalDataSourceImpl
import it.application.team_tracker.model.localDataSource.room.dao.AttachmentDAO
import it.application.team_tracker.model.localDataSource.room.dao.CommentDAO
import it.application.team_tracker.model.localDataSource.room.dao.HistoryDAO
import it.application.team_tracker.model.localDataSource.room.dao.UserDAO
import it.application.team_tracker.model.localDataSource.room.database.ApplicationDB
import it.application.team_tracker.model.remoteDataSource.RemoteDataSourceImpl
import it.application.team_tracker.model.modelsImpl.UserModelImpl
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
    fun provideAttachmentDAO(database: ApplicationDB): AttachmentDAO{
        return database.attachmentDao()
    }
    @Provides
    fun provideCommentDAO(database: ApplicationDB): CommentDAO {
        return database.commentDao()
    }
    @Provides
    fun provideHistoryDAO(database: ApplicationDB): HistoryDAO {
        return database.historyDao()
    }
    @Provides
    @Singleton
    fun provideLocalDataSource(): LocalDataSource{
        return LocalDataSourceImpl()
    }
    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource{
        return RemoteDataSourceImpl()
    }
    @Provides
    @Singleton
    fun provideUserModel(): UserModel{
        return UserModelImpl()
    }
}