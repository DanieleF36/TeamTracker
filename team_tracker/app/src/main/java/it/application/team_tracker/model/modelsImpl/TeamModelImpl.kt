package it.application.team_tracker.model.modelsImpl

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import it.application.team_tracker.model.LocalDataSource
import it.application.team_tracker.model.RemoteDataSource
import it.application.team_tracker.model.SearchFilter
import it.application.team_tracker.model.TeamModel
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.entities.Team
import it.application.team_tracker.model.exception.InternetUnavailableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeamModelImpl: TeamModel {
    private val teams = mutableStateListOf<Team?>(null)
    @Inject
    lateinit var local: LocalDataSource
    //assumption: in the absence of internet the RemoteDataSource caches the queries and will do them as soon as the internet is back
    @Inject
    lateinit var remote: RemoteDataSource
    //TODO filter
    override fun getUserTeam(userId: String, filter: SearchFilter): List<Team?> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                remote.userDao().getTeamsWithUpdate(userId).collect {
                    if (it != null) {
                        when(it.first){
                            ChangeType.ADD -> {teams.add(it.second); local.teamDao().addTeam(it.second)}
                            ChangeType.UPDATE -> {
                                val index = teams.indexOfFirst { f -> f?.id==it.second.id }
                                local.teamDao().updateTeam(teams[index]!!, it.second)
                                teams[index] = it.second
                            }
                            ChangeType.REMOVE -> {teams.remove(it.second); local.teamDao().deleteTeam(it.second.id)}
                        }
                    }
                }
            }catch (e: InternetUnavailableException){
                local.userDao().getTeams(userId).collect{
                    teams.add(it)
                }
            }
        }
        return teams.toList()
    }

    override fun addTeam(new: Team): Flow<String?> {
        if(!validate(new))
            throw IllegalArgumentException("new not well defined")
        return remote.teamDao().addTeam(new).onEach {
            if(it != null){
                local.teamDao().addTeam(new)
            }
        }
    }

    override fun updateTeam(oldTeam: Team, newTeam: Team): Flow<Boolean> {
        if(!validate(newTeam))
            throw IllegalArgumentException("newTeam is not well defined")
        if(oldTeam.id != newTeam.id)
            throw IllegalArgumentException("You are trying to update a different team from old one")

        if(oldTeam.photo != newTeam.photo){
            if(newTeam.photo == null){
                remote.teamDao().deletePhoto(newTeam.id).onCompletion {
                    if (it == null || it is InternetUnavailableException)
                        local.teamDao().deletePhoto(newTeam.id)
                }.flatMapConcat { res ->
                    if(res)
                        update(oldTeam, newTeam.copy(photo = null))
                    else
                        flowOf(false)
                }
            }
            else{
                remote.teamDao().changePhoto(newTeam.photo, newTeam.id).onCompletion {
                    if (it == null || it is InternetUnavailableException)
                        local.teamDao().changePhoto(newTeam.photo, newTeam.id)
                }.flatMapConcat { newUri ->
                    if (newUri != null)
                        update(oldTeam, newTeam.copy(photo = Uri.parse(newUri)))
                    else
                        flowOf(false)
                }
            }
        }
        return update(oldTeam, newTeam)
    }

    private fun update(old:Team, new: Team): Flow<Boolean> {
        if(!validate(new))
            throw IllegalArgumentException("User is not well defined")
        return remote.teamDao().updateTeam(old, new).onCompletion {
            if(it == null || it is InternetUnavailableException)
                local.teamDao().updateTeam(old, new)
        }
    }

    private fun validate(t: Team): Boolean{
        if(t.name.length < 3)
            return false
        if(t.creator.isBlank())
            return false
        return true
    }

    override fun deleteTeam(teamId: String): Flow<Boolean> {
        local.teamDao().deleteTeam(teamId)
        return remote.teamDao().deleteTeam(teamId)
    }

    override fun generateInvitationCode(teamId: String): Flow<String?> {
        return remote.teamDao().generateInvitationCode(teamId).onEach {
            if(it != null)
                local.teamDao().generateInvitationCode(teamId, it)
        }
    }

    override fun updateUserRole(teamId: String, userId: String, newRole: String): Flow<Boolean> {
        return remote.teamDao().updateUserRole(teamId, userId, newRole).onCompletion {
            if(it == null || it is InternetUnavailableException)
                local.teamDao().updateUserRole(teamId, userId, newRole)
        }
    }

    override fun removeUser(teamId: String, userId: String): Flow<Boolean> {
        return remote.teamDao().removeUser(teamId, userId).onCompletion {
            if(it == null || it is InternetUnavailableException)
                local.teamDao().removeUser(teamId, userId)
        }
    }

    override fun addUser(teamId: String, userId: String, role: String): Flow<Boolean> {
        return remote.teamDao().addUser(teamId, userId, role).onCompletion {
            if(it == null || it is InternetUnavailableException)
                local.teamDao().addUser(teamId, userId, role)
        }
    }
}