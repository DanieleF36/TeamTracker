package it.application.team_tracker.model.localDataSource.room.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.application.team_tracker.model.localDataSource.room.converter.Converters

@Entity(tableName = "user",
        indices = [Index("nickname"), Index("email")])
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
    val phone: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "photo")
    val photo: Uri?
)