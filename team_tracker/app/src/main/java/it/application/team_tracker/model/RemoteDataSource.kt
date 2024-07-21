package it.application.team_tracker.model

import it.application.team_tracker.model.daoes.AttachmentDAO
import it.application.team_tracker.model.daoes.HistoryDAO
import it.application.team_tracker.model.daoes.KpiDAO
import it.application.team_tracker.model.daoes.MessageDAO
import it.application.team_tracker.model.daoes.TagDAO
import it.application.team_tracker.model.daoes.TaskDAO
import it.application.team_tracker.model.daoes.TeamDAO
import it.application.team_tracker.model.daoes.UserDAO

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