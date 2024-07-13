package it.application.team_tracker.model.localDataSource.entities

import java.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.application.team_tracker.model.localDataSource.converter.Converters

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Task::class, parentColumns = ["id"], childColumns = ["task_id"], onDelete = ForeignKey.CASCADE)],
        tableName = "comment")
@TypeConverters(Converters::class)
data class Comment (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "date")
    val date: Calendar,
    @ColumnInfo(name = "task_id")
    val taskId: String,
    @ColumnInfo(name = "user_id")
    val userId: String
)