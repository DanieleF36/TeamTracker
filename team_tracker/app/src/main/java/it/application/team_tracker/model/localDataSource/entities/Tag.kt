package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag")
data class Tag (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String
)