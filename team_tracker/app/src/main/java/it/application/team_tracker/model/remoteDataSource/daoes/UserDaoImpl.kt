package it.application.team_tracker.model.remoteDataSource.daoes

import android.net.Uri
import com.google.firebase.Timestamp
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.daoes.remote.UserDAO
import it.application.team_tracker.model.entities.Feedback
import it.application.team_tracker.model.entities.Team
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Calendar

class UserDaoImpl: FirebaseDAO(), UserDAO {
    override fun getUser(userId: String): Flow<User?> {
        return getDocument<it.application.team_tracker.model.remoteDataSource.entities.User>("/users/$userId").map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getUserWithUpdate(userId: String): Flow<Pair<ChangeType, User>?>{
        return getDocumentWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.User>("/users/$userId").map {
            if(it != null)
                Pair(it.first,fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun getUserLikeNickname(nickname: String): Flow<User?> {
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.User>(db.collection("/users").whereGreaterThanOrEqualTo("nickname", nickname)).filter {
            it?.nickname?.contains(nickname) ?: false
        }.map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getUserLikeNicknameWithUpdate(nickname: String): Flow<Pair<ChangeType, User>?> {
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.User>(db.collection("/users").whereGreaterThanOrEqualTo("nickname", nickname)).filter {
            it?.second?.nickname?.contains(nickname) ?: false
        }.map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun updateUser(oldUser: User, newUser: User): Flow<Boolean> {
        return updateDocument("/users/${newUser.id}", oldUser, newUser)
    }

    override fun setFavoriteTeam(teamId: String, userId: String, add: Boolean): Flow<Boolean> {
        val old = object {val favoriteTeam = if(!add) listOf(teamId) else emptyList() }
        val new = object {val favoriteTeam = if(add) listOf(teamId) else emptyList() }
        return updateDocument("/teams/$teamId", old, new)
    }

    override fun getTeams(userId: String): Flow<Team?> = callbackFlow {
        /*val query = db.collection("/teams").whereArrayContains("members", userId)
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Team>(query).map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }*/
        getDocument<it.application.team_tracker.model.remoteDataSource.entities.User>("/users/$userId").collect{ user ->
            if(user != null) {
                val query = db.collection("/teams").whereIn("id", user.teamMembers.keys.toList())
                getCollection<it.application.team_tracker.model.remoteDataSource.entities.Team>(query).map {
                    if(it != null)
                        trySend(fromRemoteToNeutral(it))
                    else
                        trySend(null)
                }
            }
            else{
                TODO("err")
            }
        }
    }

    override fun getTeamsWithUpdate(userId: String): Flow<Pair<ChangeType, Team>?> = callbackFlow{
        /*val query = db.collection("/teams").whereArrayContains("members", userId)
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Team>(query).map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }*/
        getDocument<it.application.team_tracker.model.remoteDataSource.entities.User>("/users/$userId").collect{ user ->
            if(user != null) {
                val query = db.collection("/teams").whereIn("id", user.teamMembers.keys.toList())
                getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Team>(query).map {
                    if(it != null)
                        trySend(Pair(it.first, fromRemoteToNeutral(it.second)))
                    else
                        trySend(null)
                }
            }
            else{
                TODO("err")
            }
        }
    }

    private fun fromRemoteToNeutral(u: it.application.team_tracker.model.remoteDataSource.entities.User): User{
        return User(
            u.id,
            u.nickname,
            u.fullName,
            u.email,
            u.location,
            u.phone,
            u.description,
            if(u.photo!=null) Uri.parse(u.photo) else null,
            u.teamMembers.map { it.key },
            u.taskMembers.map { it.key },
            null
        )
    }

    private fun fromNeutralToRemote(t: Team): it.application.team_tracker.model.remoteDataSource.entities.Team{
        return it.application.team_tracker.model.remoteDataSource.entities.Team(
            t.id,
            t.name,
            t.description,
            t.invitationLink,
            t.photo.toString(),
            t.creator,
            Timestamp(t.creationDate.time),
            if(t.deliveryDate != null) Timestamp(t.deliveryDate.time) else null,
            t.teamMembers,
            t.teamMembers.keys.toList(),
            t.feedbacks.map {
                mapOf("comment" to it.comment, "evaluation" to it.evaluation.toString(), "userId" to it.userId)
            }
        )
    }

    private fun fromRemoteToNeutral(t: it.application.team_tracker.model.remoteDataSource.entities.Team): Team{
        return Team(
            t.id,
            t.name,
            t.description,
            t.invitationLink,
            if(t.photo != null) Uri.parse(t.photo) else null,
            t.creator,
            Calendar.getInstance().apply { timeInMillis = t.creationDate.seconds },
            if(t.deliveryDate != null) Calendar.getInstance().apply { timeInMillis = t.deliveryDate.seconds } else null,
            t.membersAndRole,
            t.feedbacks.map {
                Feedback("", it.get("comment")!!, it.get("evaluation")!!.toInt(), it.get("userId")!!)
            }
        )
    }
}