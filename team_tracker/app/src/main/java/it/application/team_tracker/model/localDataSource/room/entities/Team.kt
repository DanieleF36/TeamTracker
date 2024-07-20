package it.application.team_tracker.model.localDataSource.room.entities

import java.util.Calendar
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.application.team_tracker.model.localDataSource.room.converter.Converters

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["creator"], onDelete = ForeignKey.CASCADE)],
        tableName = "team",
    indices = [Index("creator")])
@TypeConverters(Converters::class)
data class Team (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "invitation_link")
    val invitationLink: String?,
    @ColumnInfo(name = "photo")
    val photo: Uri?,
    @ColumnInfo(name = "creator")
    val creator: String,
    @ColumnInfo(name = "creation_date")
    val creationDate: Calendar,
    @ColumnInfo(name = "delivery_date")
    val deliveryDate: Calendar?
)