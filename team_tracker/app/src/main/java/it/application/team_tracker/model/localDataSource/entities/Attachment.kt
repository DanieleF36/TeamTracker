package it.application.team_tracker.model.localDataSource.entities

import java.util.Calendar
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.application.team_tracker.model.localDataSource.converter.Converters

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Task::class, parentColumns = ["id"], childColumns = ["task_id"], onDelete = ForeignKey.CASCADE)],
        tableName = "attachment",
        indices = [Index("user_id"), Index("task_id")])
@TypeConverters(Converters::class)
data class Attachment (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "url")
    val url: Uri,
    @ColumnInfo(name = "creator")
    val creator: String,
    @ColumnInfo(name = "date")
    val date: Calendar,
    @ColumnInfo(name = "task_id")
    val taskId: String,
    @ColumnInfo(name = "user_id")
    val userId: String
)