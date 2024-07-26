package it.application.team_tracker.model.remoteDataSource

import it.application.team_tracker.model.RemoteDataSource
import it.application.team_tracker.model.daoes.remote.*
import it.application.team_tracker.model.remoteDataSource.firebase.daoes.*

class RemoteDataSourceImpl: RemoteDataSource {
    override fun messageDao(): MessageDAO {
        return MessageDaoImpl()
    }

    override fun taskDao(): TaskDAO {
        return TaskDaoImpl()
    }

    override fun teamDao(): TeamDAO {
        return TeamDaoImpl()
    }

    override fun userDao(): UserDAO {
        return UserDaoImpl()
    }
}