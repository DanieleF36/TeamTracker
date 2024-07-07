package it.application.team_tracker.database.local.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "nickname")
    val nickname: String,
    @ColumnInfo(name = "fullname")
    val fullname: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "phone")
    val phone: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "photo")
    val photo: Uri?,
    val teams: List<String>
)