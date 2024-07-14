package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Tag::class, parentColumns = ["id"], childColumns = ["tag_id"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Task::class, parentColumns = ["id"], childColumns = ["task_id"], onDelete = ForeignKey.CASCADE)],
        primaryKeys = ["tag_id", "task_id"],
        tableName = "tags",
        indices = [Index("tag_id"), Index("task_id")])
data class Tags (
    @ColumnInfo(name = "tag_id")
    val tagId: String,
    @ColumnInfo(name = "task_id")
    val taskId: String
)