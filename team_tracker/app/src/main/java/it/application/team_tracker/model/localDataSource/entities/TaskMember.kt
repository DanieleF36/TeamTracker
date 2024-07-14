package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Task::class, parentColumns = ["id"], childColumns = ["task_id"], onDelete = ForeignKey.CASCADE)],
        tableName = "task_member",
        primaryKeys = ["user_id", "task_id"],
        indices = [Index("user_id"), Index("task_id")])
data class TaskMember(
    @ColumnInfo(name="user_id")
    val userId: String,
    @ColumnInfo(name="task_id")
    val taskId: String,
    @ColumnInfo(name = "role")
    val role: String
)