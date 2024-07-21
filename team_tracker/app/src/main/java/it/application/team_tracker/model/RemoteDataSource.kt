package it.application.team_tracker.model

import it.application.team_tracker.model.daoes.remote.AttachmentDAO
import it.application.team_tracker.model.daoes.remote.HistoryDAO
import it.application.team_tracker.model.daoes.remote.KpiDAO
import it.application.team_tracker.model.daoes.remote.MessageDAO
import it.application.team_tracker.model.daoes.remote.TagDAO
import it.application.team_tracker.model.daoes.remote.TaskDAO
import it.application.team_tracker.model.daoes.remote.TeamDAO
import it.application.team_tracker.model.daoes.remote.UserDAO

interface RemoteDataSource {
    fun attachmentDao(): AttachmentDAO
    //fun commentDao(): CommentDAO
    fun historyDao(): HistoryDAO
    fun kpiDao(): KpiDAO
    fun messageDao(): MessageDAO
    fun tagDao(): TagDAO
    fun taskDao(): TaskDAO
    fun teamDao(): TeamDAO
    fun userDao(): UserDAO
}