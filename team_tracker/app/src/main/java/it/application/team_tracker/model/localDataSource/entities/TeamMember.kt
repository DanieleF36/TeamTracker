package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Team::class, parentColumns = ["id"], childColumns = ["team_id"], onDelete = ForeignKey.CASCADE)],
        tableName = "team_member",
        primaryKeys = ["user_id", "team_id"],
    indices = [Index("user_id"), Index("team_id")])
data class TeamMember(
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "team_id")
    val teamId: String,
    @ColumnInfo(name = "role")
    val role: String
)