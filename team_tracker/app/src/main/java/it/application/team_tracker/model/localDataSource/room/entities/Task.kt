package it.application.team_tracker.model.localDataSource.room.entities

import java.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.application.team_tracker.model.localDataSource.room.converter.Converters

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["closer"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["creator"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Team::class, parentColumns = ["id"], childColumns = ["team_id"], onDelete = ForeignKey.CASCADE)],
        tableName = "task",
        indices = [Index("closer"), Index("creator"), Index("team_id")])
@TypeConverters(Converters::class)
data class Task (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "due_date")
    val dueDate: Calendar,
    @ColumnInfo(name = "creation_date")
    val creationDate: Calendar,
    @ColumnInfo(name = "closing_date")
    val closingDate: Calendar,
    @ColumnInfo(name = "state")
    val state: Int,
    @ColumnInfo(name = "creator")
    val creator: String,
    @ColumnInfo(name = "closer")
    val closer: String?,
    @ColumnInfo(name = "priority")
    val priority: Int,
    @ColumnInfo(name = "team_id")
    val teamId: String,
    @ColumnInfo(name = "time_spent")
    val timeSpent: Float,
)