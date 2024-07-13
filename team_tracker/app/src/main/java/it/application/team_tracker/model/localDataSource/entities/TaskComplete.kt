package it.application.team_tracker.model.localDataSource.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TaskComplete (
    @Embedded
    val task: Task,
    @Relation(parentColumn = "id", entityColumn = "task_id")
    val histories: List<History>,
    @Relation(parentColumn = "id",
              entityColumn = "id",
              associateBy = Junction(Tags::class, parentColumn = "task_id", entityColumn = "tag_id"))
    val tags:List<Tag>,
    @Relation(parentColumn = "id", entityColumn = "task_id")
    val attachments: List<Attachment>,
    @Relation(parentColumn = "id", entityColumn = "task_id")
    val comments: List<Comment>,
    @Relation(parentColumn = "id",
              entityColumn = "id",
              associateBy = Junction(TaskMember::class, parentColumn = "task_id", entityColumn = "user_id"))
    val users: List<User>
)