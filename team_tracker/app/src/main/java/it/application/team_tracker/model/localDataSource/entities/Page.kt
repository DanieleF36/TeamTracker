package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Team::class, parentColumns = ["id"], childColumns = ["team_id"], onDelete = ForeignKey.CASCADE)],
        tableName = "page")
data class Page (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "team_id")
    val teamId: String
)