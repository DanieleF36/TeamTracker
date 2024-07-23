package it.application.team_tracker.model.remoteDataSource.daoes

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.daoes.remote.MessageDAO
import it.application.team_tracker.model.entities.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Date

class MessageDaoImpl: FirebaseDAO(), MessageDAO {
    private fun getMessage(teamId: String? = null, receiverId: String? = null, userId: String, messageId: String? = null, onMessage: (it.application.team_tracker.model.remoteDataSource.entities.Message?)->Unit){
        if((teamId != null && receiverId != null && messageId != null) || (teamId == null && receiverId == null && messageId == null))
            throw IllegalArgumentException("One between teamId, receiverId and messageId has to be not null meanwhile the others have to be null")
        if(teamId != null) {
            val query =  db.collection("/messages").whereEqualTo("teamId", teamId).whereArrayContains("isLastRead", userId)
            query.get().addOnSuccessListener { mes ->
                onMessage(mes.documents[0].toObject(it.application.team_tracker.model.remoteDataSource.entities.Message::class.java))
            }.addOnFailureListener {
                TODO()
            }
        //return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Message>(query)
        }
        if(receiverId != null) {
            val query = db.collection("/messages").whereEqualTo("receiver", receiverId).whereArrayContains("isLastRead", userId)
            query.get().addOnSuccessListener { mes ->
                onMessage(mes.documents[0].toObject(it.application.team_tracker.model.remoteDataSource.entities.Message::class.java))
            }.addOnFailureListener {
                TODO()
            }
            //return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Message>(query)
        }
        db.document("/messages/$messageId").get().addOnSuccessListener { mes ->
            onMessage(mes.toObject(it.application.team_tracker.model.remoteDataSource.entities.Message::class.java))
        }.addOnFailureListener {
            TODO()
        }
        //return getDocument<it.application.team_tracker.model.remoteDataSource.entities.Message>("/messages/$messageId")
    }

    override fun getTeamMessages(teamId: String, monthBehind: Int): Flow<Message?> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, -monthBehind)
        val query = db.collection("/messages").whereEqualTo("receiver", null).whereEqualTo("teamId", teamId).whereGreaterThanOrEqualTo("date", Timestamp(date = calendar.time))
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Message>(query).map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getTeamMessagesWithUpdate(teamId: String, monthBehind: Int): Flow<Pair<ChangeType, Message>?> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, -monthBehind)
        val query = db.collection("/messages").whereEqualTo("receiver", null).whereEqualTo("teamId", teamId).whereGreaterThanOrEqualTo("date", Timestamp(date = calendar.time))
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Message>(query).map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun getPrivateMessages(userId: String, monthBehind: Int): Flow<Message?> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, -monthBehind)
        val query = db.collection("/messages").whereEqualTo("receiver", userId).whereEqualTo("teamId", null).whereGreaterThanOrEqualTo("date", Timestamp(date = calendar.time))
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Message>(query).map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getPrivateMessagesWithUpdate(userId: String, monthBehind: Int): Flow<Pair<ChangeType, Message>?> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, -monthBehind)
        val query = db.collection("/messages").whereEqualTo("receiver", userId).whereEqualTo("teamId", null).whereGreaterThanOrEqualTo("date", Timestamp(date = calendar.time))
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Message>(query).map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun sendTeamMessage(teamId: String, message: Message): Flow<String?> {
        return addDocument("/messages", fromNeutralToRemote(message))
    }

    override fun sendPrivateMessage(userId: String, message: Message): Flow<String?> {
        return addDocument("/messages", fromNeutralToRemote(message))
    }

    override fun setLastTeamReadMessage(teamId: String, userId: String, messageId: String): Flow<Boolean> = callbackFlow {
        getMessage(teamId = teamId, userId = userId){ m->
            val tra = db.batch()
            tra.update(
                db.document("/messages/${m!!.id}"),
                "isLastRead",
                FieldValue.arrayRemove(userId)
            )
            tra.update(
                db.document("/messages/$messageId"),
                "isLastRead",
                FieldValue.arrayUnion(userId)
            )
            tra.commit().addOnSuccessListener { trySend(true) }.addOnFailureListener { trySend(false) }
        }
    }

    override fun setLastPrivateReadMessage(receiverId: String, userId: String, messageId: String): Flow<Boolean> = callbackFlow {
        getMessage(receiverId = receiverId, userId = userId){ m->
            val tra = db.batch()
            tra.update(
                db.document("/messages/${m!!.id}"), "isLastRead", FieldValue.arrayRemove(userId))
            tra.update(
                db.document("/messages/$messageId"), "isLastRead", FieldValue.arrayUnion(userId))
            tra.commit().addOnSuccessListener { trySend(true) }.addOnFailureListener { trySend(false) }
        }
    }
    //TODO da controllare
    override fun getUnreadTeamMessageCount(userId: String, teamId: String): Flow<Int> = callbackFlow{
        val query1 = db.collection("/messages").whereEqualTo("teamId", teamId).whereArrayContains("isLastRead", userId)
        query1.get().addOnSuccessListener { snap ->
            if (snap == null)
                trySend(0)
            else {
                val message = snap.documents[0].toObject(it.application.team_tracker.model.remoteDataSource.entities.Message::class.java)
                val query2 = db.collection("/messages").orderBy("date", Query.Direction.ASCENDING).startAt(message!!.date)
                query2.get().addOnSuccessListener {
                    trySend(it.size()-1)
                }.addOnFailureListener {
                    TODO()
                }
            }
        }
    }
    //TODO da controllare
    override fun getUnreadTeamMessageCountWithUpdate(userId: String, teamId: String): Flow<Int> = callbackFlow {
        val query1 = db.collection("/messages").whereEqualTo("teamId", teamId)
            .whereArrayContains("isLastRead", userId)
        val listener = query1.addSnapshotListener { snap, err ->
            if (err != null)
                TODO()
            else {
                if (snap == null)
                    trySend(0)
                else {
                    val message =
                        snap.documents[0].toObject(it.application.team_tracker.model.remoteDataSource.entities.Message::class.java)
                    val query2 =
                        db.collection("/messages").orderBy("date", Query.Direction.ASCENDING)
                            .startAt(message!!.date)
                    query2.get().addOnSuccessListener {
                        trySend(it.size() - 1)
                    }.addOnFailureListener {
                        TODO()
                    }
                }
            }
        }
        awaitClose { listener.remove() }
    }
    //TODO da controllare
    override fun getUnreadPrivateMessageCount(receiverId: String, userId: String): Flow<Int> = callbackFlow{
        val query1 = db.collection("/messages").whereEqualTo("receiver", receiverId).whereArrayContains("isLastRead", userId)
        query1.get().addOnSuccessListener { snap ->
            if (snap == null)
                trySend(0)
            else {
                val message = snap.documents[0].toObject(it.application.team_tracker.model.remoteDataSource.entities.Message::class.java)
                val query2 = db.collection("/messages").orderBy("date", Query.Direction.ASCENDING).startAt(message!!.date)
                query2.get().addOnSuccessListener {
                    trySend(it.size()-1)
                }.addOnFailureListener {
                    TODO()
                }
            }
        }
    }
    //TODO da controllare
    override fun getUnreadPrivateMessageCountWithUpdate(receiverId: String, userId: String): Flow<Int> = callbackFlow {
        val query1 = db.collection("/messages").whereEqualTo("receiver", receiverId).whereArrayContains("isLastRead", userId)
        val listener = query1.addSnapshotListener { snap, err ->
            if (err != null)
                TODO()
            else {
                if (snap == null)
                    trySend(0)
                else {
                    val message =
                        snap.documents[0].toObject(it.application.team_tracker.model.remoteDataSource.entities.Message::class.java)
                    val query2 =
                        db.collection("/messages").orderBy("date", Query.Direction.ASCENDING)
                            .startAt(message!!.date)
                    query2.get().addOnSuccessListener {
                        trySend(it.size() - 1)
                    }.addOnFailureListener {
                        TODO()
                    }
                }
            }
        }
        awaitClose { listener.remove() }
    }

    private fun fromRemoteToNeutral(m: it.application.team_tracker.model.remoteDataSource.entities.Message): Message{
        return Message(
            m.id,
            m.message,
            Calendar.getInstance().apply { timeInMillis = m.date.seconds },
            m.teamId,
            m.sender,
            m.receiver,
            m.isLastRead.filter {
                    Firebase.auth.currentUser?.uid == it
                }.size == 1
        )
    }

    private fun fromNeutralToRemote(m: Message): it.application.team_tracker.model.remoteDataSource.entities.Message{
        return it.application.team_tracker.model.remoteDataSource.entities.Message(
            m.id,
            m.message,
            Timestamp(m.date.time),
            m.teamId,
            m.sender,
            m.receiver,
            if(Firebase.auth.currentUser != null)listOf(Firebase.auth.currentUser!!.uid) else emptyList()
        )
    }
}