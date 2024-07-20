package it.application.team_tracker.model.remoteDataSource

import it.application.team_tracker.model.DataSource
import it.application.team_tracker.model.daoes.*
import it.application.team_tracker.model.remoteDataSource.daoes.*

class RemoteDataSource: DataSource {
    override fun attachmentDao(): AttachmentDAO {
        return AttachmentDAO()
    }

    override fun historyDao(): HistoryDAO {
        TODO("Not yet implemented")
    }

    override fun kpiDao(): KpiDAO {
        TODO("Not yet implemented")
    }

    override fun messageDao(): MessageDAO {
        TODO("Not yet implemented")
    }

    override fun tagDao(): TagDAO {
        TODO("Not yet implemented")
    }

    override fun taskDao(): TaskDAO {
        TODO("Not yet implemented")
    }

    override fun teamDao(): TeamDAO {
        TODO("Not yet implemented")
    }

    override fun userDao(): UserDAO {
        TODO("Not yet implemented")
    }
}