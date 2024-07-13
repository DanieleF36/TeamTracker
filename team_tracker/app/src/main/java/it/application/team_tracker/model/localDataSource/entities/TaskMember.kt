package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Task::class, parentColumns = ["id"], childColumns = ["taskId"], onDelete = ForeignKey.CASCADE)],
        tableName = "task_member",
        primaryKeys = ["userId", "taskId"])
data class TaskMember(
    val userId: String,
    val taskId: String,
    @ColumnInfo(name = "role")
    val role: String
)