package it.application.team_tracker.model.localDataSource

import it.application.team_tracker.model.LocalDataSource
import it.application.team_tracker.model.daoes.remote.AttachmentDAO
import it.application.team_tracker.model.daoes.remote.HistoryDAO
import it.application.team_tracker.model.daoes.remote.KpiDAO
import it.application.team_tracker.model.daoes.remote.MessageDAO
import it.application.team_tracker.model.daoes.remote.TagDAO
import it.application.team_tracker.model.daoes.remote.TaskDAO
import it.application.team_tracker.model.daoes.remote.TeamDAO
import it.application.team_tracker.model.daoes.remote.UserDAO
import it.application.team_tracker.model.entities.Attachment
import it.application.team_tracker.model.entities.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl: LocalDataSource {
    override fun attachmentDao(): AttachmentDAO {
        return object : AttachmentDAO {
            @Inject
            lateinit var dao: it.application.team_tracker.model.localDataSource.room.dao.AttachmentDAO
            override fun getTaskAttachments(taskId: String, listenForUpdates: Boolean): Flow<Attachment?> {
                if(listenForUpdates)
                    throw IllegalArgumentException("Can not listen for update in a local db")
                return dao.getAttachmentsByTask(taskId).map{ if(it != null) fromLocalToNeutral(it) else null }
            }

            override fun getAttachment(idAttachment: String, listenForUpdates: Boolean): Flow<Attachment?> {
                if(listenForUpdates)
                    throw IllegalArgumentException("Can not listen for update in a local db")
                return dao.getAttachment(idAttachment).map{ if(it != null) fromLocalToNeutral(it) else null }
            }

            override fun addAttachment(attachment: Attachment): Flow<String> = flow{
                dao.addAttachment(fromNeutralToLocal(attachment))
                emit(attachment.id)
            }
            private fun fromLocalToNeutral(a: it.application.team_tracker.model.localDataSource.room.entities.Attachment): Attachment{
                return Attachment(a.id, a.url, a.date, a.taskId, a.userId)
            }
            private fun fromNeutralToLocal(a: Attachment): it.application.team_tracker.model.localDataSource.room.entities.Attachment{
                return it.application.team_tracker.model.localDataSource.room.entities.Attachment(a.id, a.url, a.date, a.taskId, a.userId)
            }
        }
    }

    /*override fun commentDao(): CommentDAO {
        return object: CommentDAO{
            @Inject
            lateinit var dao: it.application.team_tracker.model.localDataSource.room.dao.CommentDAO
            override fun getTaskComments(taskId: String, listenForUpdates: Boolean): Flow<Comment?> {
                if(listenForUpdates)
                    throw IllegalArgumentException("Can not listen for update in a local db")
                return dao.getCommentsByTask(taskId).map{ if(it != null)fromLocalToNeutral(it)else null }
            }

            override fun addComment(comment: Comment): Flow<String> = flow {
                dao.addComment(fromNeutralToLocal(comment))
                emit(comment.id)
            }

            fun fromLocalToNeutral(c: it.application.team_tracker.model.localDataSource.room.entities.Comment): Comment{
                return Comment(c.id, c.message, c.date, c.taskId, c.userId)
            }

            fun fromNeutralToLocal(c:Comment): it.application.team_tracker.model.localDataSource.room.entities.Comment{
                return it.application.team_tracker.model.localDataSource.room.entities.Comment(c.id, c.message, c.date, c.taskId, c.userId)
            }

        }
    }*/

    override fun historyDao(): HistoryDAO {
        return object: HistoryDAO {
            @Inject
            lateinit var dao: it.application.team_tracker.model.localDataSource.room.dao.HistoryDAO
            override fun getTaskHistory(taskId: String, listenForUpdates: Boolean): Flow<History?> {
                if(listenForUpdates)
                    throw IllegalArgumentException("Can not listen for update in a local db")
                return dao.getHistoriesByTask(taskId).map {if(it!=null)fromLocalToNeutral(it)else null}

            }
            fun fromLocalToNeutral(h: it.application.team_tracker.model.localDataSource.room.entities.History): History{
                return History(h.id, h.type, h.message, h.date, h.taskId)
            }
        }
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