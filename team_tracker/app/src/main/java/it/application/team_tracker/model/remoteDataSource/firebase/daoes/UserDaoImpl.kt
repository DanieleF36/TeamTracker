package it.application.team_tracker.model.remoteDataSource.firebase.daoes

import android.content.Context
import android.net.Uri
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.daoes.remote.UserDAO
import it.application.team_tracker.model.entities.Feedback
import it.application.team_tracker.model.entities.Team
import it.application.team_tracker.model.entities.User
import it.application.team_tracker.model.exception.NoAccountAvailableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class UserDaoImpl: FirebaseDAO(), UserDAO {
    override fun login(): Flow<String?> {
        TODO("Not yet implemented")
    }

    override fun loginGoogle(context: Context?): Flow<String?> = flow {
        if(context == null)
            throw IllegalArgumentException("Here google credential manager is used so context is needed")
        val auth = Firebase.auth

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId("TODO WEB_CLIENT_ID")
            .setAutoSelectEnabled(true)
            .setNonce(randomString(32))
        .build()

        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption)

        CoroutineScope(Dispatchers.IO).launch{
            val credentialManager = CredentialManager.create(context)
            try {
                val result = credentialManager.getCredential(request = request.build(),context = context)
                when(val credential = result.credential){
                    is CustomCredential -> {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        val user = auth.signInWithCredential(firebaseCredential).await().user
                        if(user != null)
                            emit(user.uid)
                        else
                            emit(null)

                    }
                    else -> {
                        throw IllegalStateException("Internal error")
                    }
                }
            } catch (e: GetCredentialException) {
                if(e.type == android.credentials.GetCredentialException.TYPE_INTERRUPTED)
                    throw IllegalStateException("User has interrupted login action")
                if(e.type == android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL)
                    throw NoAccountAvailableException("No account available for the login")
                throw e
            }
        }
    }

    override fun getUser(userId: String): Flow<User?> {
        return getDocument<it.application.team_tracker.model.remoteDataSource.firebase.entities.User>("/users/$userId").map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getUserWithUpdate(userId: String): Flow<Pair<ChangeType, User>?>{
        return getDocumentWithUpdate<it.application.team_tracker.model.remoteDataSource.firebase.entities.User>("/users/$userId").map {
            if(it != null)
                Pair(it.first,fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun getUserLikeNickname(nickname: String): Flow<User?> {
        return getCollection<it.application.team_tracker.model.remoteDataSource.firebase.entities.User>(db.collection("/users").whereGreaterThanOrEqualTo("nickname", nickname)).filter {
            it?.nickname?.contains(nickname) ?: false
        }.map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getUserLikeNicknameWithUpdate(nickname: String): Flow<Pair<ChangeType, User>?> {
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.firebase.entities.User>(db.collection("/users").whereGreaterThanOrEqualTo("nickname", nickname)).filter {
            it?.second?.nickname?.contains(nickname) ?: false
        }.map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun updateUser(oldUser: User, newUser: User): Flow<Boolean> {
        if(oldUser.photo != newUser.photo){
            throw IllegalStateException("To change the photo you should call changePhoto")
        }
        return updateDocument("/users/${newUser.id}", oldUser, newUser)
    }

    override fun changePhoto(uri: Uri, userId: String): Flow<String?> {
        return uploadPhoto(uri, true, userId)
    }

    override fun deletePhoto(userId: String): Flow<Boolean> {
        return super.deletePhoto(true, userId)
    }

    override fun setFavoriteTeam(teamId: String, userId: String, add: Boolean): Flow<Boolean> {
        val old = object {val favoriteTeam = if(!add) listOf(teamId) else emptyList() }
        val new = object {val favoriteTeam = if(add) listOf(teamId) else emptyList() }
        return updateDocument("/teams/$teamId", old, new)
    }

    override fun getTeams(userId: String): Flow<Team?> = callbackFlow {
        getDocument<it.application.team_tracker.model.remoteDataSource.firebase.entities.User>("/users/$userId").collect{ user ->
            if(user != null) {
                val query = db.collection("/teams").whereIn("id", user.teamMembers.keys.toList())
                getCollection<it.application.team_tracker.model.remoteDataSource.firebase.entities.Team>(query).map {
                    if(it != null)
                        trySend(fromRemoteToNeutral(it))
                    else
                        trySend(null)
                }
            }
            else{
                throw IllegalArgumentException("user is not available")
            }
        }
    }

    override fun getTeamsWithUpdate(userId: String): Flow<Pair<ChangeType, Team>?> = callbackFlow{
        getDocumentWithUpdate<it.application.team_tracker.model.remoteDataSource.firebase.entities.User>("/users/$userId").collect{ user ->
            if(user != null) {
                val query = db.collection("/teams").whereIn("id", user.second.teamMembers.keys.toList())
                getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.firebase.entities.Team>(query).map {
                    if(it != null)
                        trySend(Pair(it.first, fromRemoteToNeutral(it.second)))
                    else
                        trySend(null)
                }
            }
            else{
                throw IllegalArgumentException("user is not available")
            }
        }
    }

    private fun fromRemoteToNeutral(u: it.application.team_tracker.model.remoteDataSource.firebase.entities.User): User{
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

    private fun fromNeutralToRemote(t: Team): it.application.team_tracker.model.remoteDataSource.firebase.entities.Team {
        return it.application.team_tracker.model.remoteDataSource.firebase.entities.Team(
            t.id,
            t.name,
            t.description,
            t.invitationLink,
            t.photo.toString(),
            t.creator,
            Timestamp(t.creationDate.time),
            if (t.deliveryDate != null) Timestamp(t.deliveryDate.time) else null,
            t.teamMembers,
            t.teamMembers.keys.toList(),
            t.feedbacks.map {
                mapOf(
                    "comment" to it.comment,
                    "evaluation" to it.evaluation.toString(),
                    "userId" to it.userId
                )
            }
        )
    }

    private fun fromRemoteToNeutral(t: it.application.team_tracker.model.remoteDataSource.firebase.entities.Team): Team{
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