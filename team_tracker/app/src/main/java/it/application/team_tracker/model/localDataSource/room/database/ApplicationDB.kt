package it.application.team_tracker.model.localDataSource.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.application.team_tracker.model.localDataSource.room.dao.*
import it.application.team_tracker.model.localDataSource.room.entities.*

@Database(entities = [Attachment::class, Comment::class, Feedback::class,
                      History::class, Kpi::class, Message::class, Page::class,
                      Tag::class, Tags::class, Task::class, TaskMember::class,
                      Team::class, TeamMember::class, User::class],
          version = 1)
abstract class ApplicationDB: RoomDatabase() {
    abstract fun attachmentDao(): AttachmentDAO
    abstract fun commentDao(): CommentDAO
    abstract fun feedbackDao(): FeedbackDAO
    abstract fun historyDao(): HistoryDAO
    abstract fun kpiDao(): KpiDAO
    abstract fun messageDao(): MessageDAO
    abstract fun pageDao(): PageDAO
    abstract fun tagDao(): TagDAO
    abstract fun tagsDao(): TagsDAO
    abstract fun taskDao(): TaskDAO
    abstract fun taskComplete(): TaskCompleteDAO
    abstract fun teamDao(): TeamDAO
    abstract fun teamCompleteDao(): TeamCompleteDAO
    abstract fun teamMemberDao(): TeamMemberDAO
    abstract fun userDao(): UserDAO
    abstract fun userWithTeamDao(): UserWithTeamDAO
}