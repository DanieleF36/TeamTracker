package it.application.team_tracker.model.localDataSource.room.entities

import java.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.application.team_tracker.model.localDataSource.room.converter.Converters

@Entity(foreignKeys = [ForeignKey(entity = Team::class, parentColumns = ["id"], childColumns = ["team_id"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["sender"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["receiver"], onDelete = ForeignKey.CASCADE)],
        tableName = "message",
        indices = [Index("team_id"), Index("sender"), Index("receiver")])
@TypeConverters(Converters::class)
data class Message (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "date")
    val date: Calendar,
    @ColumnInfo(name = "team_id")
    val teamId: String?,
    @ColumnInfo(name = "sender")
    val sender: String,
    @ColumnInfo(name = "receiver")
    val receiver: String?
)