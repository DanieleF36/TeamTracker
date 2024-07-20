package it.application.team_tracker.model

import it.application.team_tracker.model.daoes.*

interface DataSource {
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