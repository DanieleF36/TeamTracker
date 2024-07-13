package it.application.team_tracker.model.localDataSource.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.application.team_tracker.model.localDataSource.converter.Converters

@Entity(tableName = "user")
@TypeConverters(Converters::class)
data class User (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "nickname")
    val nickname: String,
    @ColumnInfo(name = "fullname")
    val fullName: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "phone")
    val phone: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "photo")
    val photo: Uri?
)