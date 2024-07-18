package it.application.team_tracker.model.remoteDataSource.daoes.implementations

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import it.application.team_tracker.model.remoteDataSource.daoes.interfaces.UserDAO
import it.application.team_tracker.model.remoteDataSource.entities.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserDaoImpl: UserDAO {
    val db = Firebase.firestore
    override fun getUser(id: String, listenUpdate: Boolean): Flow<User?> = callbackFlow {
        val docRef = db.collection("/user").document(id)
        if(listenUpdate){
            val listener = docRef.addSnapshotListener { doc, error ->
                if(error == null){
                    trySend(doc?.toObject(User::class.java))
                }
                else
                    throw error
            }
            awaitClose { listener.remove() }
        }
        else
            docRef.get().addOnSuccessListener { doc ->
                trySend(doc?.toObject(User::class.java))
            }
    }

    override fun getByNickname(nickname: String, listenUpdate: Boolean): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun getLikeNickname(nickname: String, listenUpdate: Boolean): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun addUser(user: User, listenUpdate: Boolean): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun addUsers(vararg user: User, listenUpdate: Boolean): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User, listenUpdate: Boolean): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(id: String, listenUpdate: Boolean): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}