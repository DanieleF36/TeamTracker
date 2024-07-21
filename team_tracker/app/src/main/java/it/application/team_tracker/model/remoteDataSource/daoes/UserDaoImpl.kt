package it.application.team_tracker.model.remoteDataSource.daoes

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import it.application.team_tracker.model.daoes.remote.UserDAO
import it.application.team_tracker.model.entities.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserDaoImpl: UserDAO {
    private val db = Firebase.firestore

    override fun getUser(userId: String, listenForUpdates: Boolean): Flow<User?> = callbackFlow {
        val docRef = db.collection("/user").document(userId)
        if(listenForUpdates){
            val listener = docRef.addSnapshotListener { doc, error ->
                if(error == null){
                    trySend(doc?.toObject(User::class.java))
                }
                else
                    throw error
            }
            awaitClose { listener.remove() }
        }
        else {
            docRef.get().addOnSuccessListener { doc ->
                trySend(doc?.toObject(User::class.java))
            }.addOnFailureListener {
                err->
                err
                //TODO
            }
        }

    }

    override fun getUserLikeNickname(
        nickname: String,
        listenForUpdates: Boolean
    ): Flow<it.application.team_tracker.model.entities.User?> {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: it.application.team_tracker.model.entities.User): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun addUser(user: it.application.team_tracker.model.entities.User): Flow<String> {
        TODO("Not yet implemented")
    }
}