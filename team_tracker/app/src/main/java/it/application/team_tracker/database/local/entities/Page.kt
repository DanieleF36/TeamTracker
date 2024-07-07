package it.application.team_tracker.database.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page")
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