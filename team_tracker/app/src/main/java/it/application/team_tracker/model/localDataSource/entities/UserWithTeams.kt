package it.application.team_tracker.model.localDataSource.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * This was created just to get all the team of one user
 */
data class UserWithTeams(
    @Embedded
    val user: User,
    @Relation(parentColumn = "id",
              entityColumn = "id",
              associateBy = Junction(TeamMember::class, parentColumn = "user_id", entityColumn = "team_id")
    )
    val teams: List<Team>
)