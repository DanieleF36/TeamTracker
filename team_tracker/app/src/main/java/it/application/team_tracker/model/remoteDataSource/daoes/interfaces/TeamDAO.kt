package it.application.team_tracker.model.remoteDataSource.daoes.interfaces

import it.application.team_tracker.model.remoteDataSource.entities.Team

interface TeamDAO {
    fun getFullTeam(id: String): Team
    fun getTeamsByUser(idUser: String): List<Team>
    fun addTeams(vararg team: Team)
    fun addTeamMembers(teamId: String, vararg userId: String)
    fun updateTeam(team: Team)
    fun deleteTeam(id: String)
}