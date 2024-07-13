package it.application.team_tracker.model.localDataSource.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TeamComplete(
    @Embedded
    val team: Team,
    @Relation(parentColumn = "id", entityColumn = "team_id")
    val tasks: List<Task>,
    @Relation(parentColumn = "id",
              entityColumn = "id",
              associateBy = Junction(TeamMember::class, parentColumn = "team_id", entityColumn = "user_id"))
    val users: List<UserWithRole>
)

data class UserWithRole(
    @Embedded
    val user: User,
    val role: String
)