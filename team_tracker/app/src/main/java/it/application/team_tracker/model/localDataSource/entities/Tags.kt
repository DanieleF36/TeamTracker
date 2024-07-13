package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Tag::class, parentColumns = ["id"], childColumns = ["tag_id"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Task::class, parentColumns = ["id"], childColumns = ["task_id"], onDelete = ForeignKey.CASCADE)],
        tableName = "tags")
data class Tags (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "tag_id")
    val tagId: String,
    @ColumnInfo(name = "task_id")
    val taskId: String
)