package it.application.team_tracker.model.remoteDataSource.daoes

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.daoes.remote.TeamDAO
import it.application.team_tracker.model.entities.Feedback
import it.application.team_tracker.model.entities.Team
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class TeamDaoImpl: FirebaseDAO(), TeamDAO {
    override fun updateTeam(oldTeam: Team, newTeam: Team): Flow<Boolean> {
        return updateDocument(newTeam.id, oldTeam, newTeam)
    }

    override fun deleteTeam(teamId: String): Flow<Boolean> {
        return deleteDocument("/teams/$teamId")
    }

    override fun updateUserRole(teamId: String, userId: String, newRole: String): Flow<Boolean> = callbackFlow{
        val batch = db.batch()
        batch.update(db.document("/team/$teamId"),"membersAndRole.$userId", FieldValue.delete())
        batch.update(db.document("/tasks/$teamId"),"membersAndRole.$userId", newRole)
        batch.commit().addOnSuccessListener {
            trySend(true)
        }.addOnFailureListener {
            trySend(false)
        }
    }

    override fun removeUser(teamId: String, userId: String): Flow<Boolean> = callbackFlow{
        val batch = db.batch()
        batch.update(db.document("/team/$teamId"),"membersAndRole.$userId", FieldValue.delete())
        batch.update(db.document("/tasks/$teamId"),"members", FieldValue.arrayRemove(userId))
        batch.commit().addOnSuccessListener {
            trySend(true)
        }.addOnFailureListener {
            trySend(false)
        }
    }

    override fun generateInvitationCode(teamId: String): Flow<String?> {
        val random = randomString(32)
        val old = object{val invitationLink = ""}
        val new = object{val invitationLink = random}
        return updateDocument("/teams/$teamId", old, new).map {
            if(it)
                random
            else
                null
        }
    }

    override fun addTeam(team: Team): Flow<String?> {
        return addDocument("/teams", fromNeutralToRemote(team))
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
            if(t.deliveryDate != null)Timestamp(t.deliveryDate.time) else null,
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
            if(t.deliveryDate != null)Calendar.getInstance().apply { timeInMillis = t.deliveryDate.seconds } else null,
            t.membersAndRole,
            t.feedbacks.map {
                Feedback("", it.get("comment")!!, it.get("evaluation")!!.toInt(), it.get("userId")!!)
            }
        )
    }
}