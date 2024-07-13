package it.application.team_tracker.model.localDataSource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
                       ForeignKey(entity = Team::class, parentColumns = ["id"], childColumns = ["teamId"], onDelete = ForeignKey.CASCADE)],
        tableName = "team_member",
        primaryKeys = ["userId", "teamId"])
data class TeamMember(
    val userId: String,
    val teamId: String,
    @ColumnInfo(name = "role")
    val role: String
)