package it.application.team_tracker.model.remoteDataSource.firebase.daoes

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.remoteDataSource.firebase.entities.Entity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.reflect.full.declaredMemberProperties

abstract class FirebaseDAO {
    protected val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance().reference

    protected fun toChangeType(change: DocumentChange): ChangeType {
        return when(change.type) {
            DocumentChange.Type.ADDED -> ChangeType.ADD
            DocumentChange.Type.MODIFIED -> ChangeType.UPDATE
            DocumentChange.Type.REMOVED -> ChangeType.REMOVE
        }
    }

    protected inline fun <reified T> getCollectionWithUpdate(query: Query): Flow<Pair<ChangeType, T>?> = callbackFlow {
        val listener =
            query.addSnapshotListener{ value, err ->
                if(err != null) {
                    TODO()
                }else {
                    if(value != null) {
                        value.documentChanges.forEach {
                            trySend(Pair(toChangeType(it), it.document.toObject(T::class.java)))
                        }
                    }
                    else
                        trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    protected inline fun <reified T> getCollection(query: Query): Flow<T?> = callbackFlow {
        query.get().addOnSuccessListener { value->
            if(value != null) {
                value.documentChanges.forEach {
                    trySend(it.document.toObject(T::class.java))
                }
            }
            else
                trySend(null)
        }.addOnFailureListener { err->
            TODO()
            //throw err
        }
    }

    protected inline fun <reified T> getDocumentWithUpdate(documentPath: String): Flow<Pair<ChangeType, T>?> = callbackFlow {
        val listener = db.document(documentPath).addSnapshotListener { value, err ->
            if(err != null) {
                TODO()
                //throw err
            }else{
                if(value != null) {
                    if(value.exists())
                        trySend(Pair(ChangeType.UPDATE, value.toObject(T::class.java)!!))
                    else
                        trySend(Pair(ChangeType.REMOVE, value.toObject(T::class.java)!!))
                }
                else {
                    trySend(null)
                }
            }
        }
        awaitClose { listener.remove() }
    }

    protected inline fun <reified T> getDocument(documentPath: String): Flow<T?> = callbackFlow {
        db.document(documentPath).get().addOnSuccessListener { value ->
            if(value != null)
                trySend(value.toObject(T::class.java))
            else
                trySend(null)
        }.addOnFailureListener {
            TODO()
        }
    }

    /**
     * Creates a new one. If the provided `obj` implements
     *
     * @param collectionPath a String
     * @return a Flow<Boolean> that emits `true` in case of success or `false` in case of error
     *
     */
    protected fun addDocument(collectionPath: String, obj: Entity): Flow<String?> = callbackFlow {
        db.collection(collectionPath).add(obj).addOnSuccessListener {
            trySend(it.id)
        }.addOnFailureListener {
            TODO()
        }
    }

    /**
     * Updates an already existing document.
     *
     * @param documentPath a String
     * @return a Flow<Boolean> that emits `true` in case of success or `false` in case of error
     *
     */
    protected fun <T : Any> updateDocument(documentPath: String, old: T, new: T): Flow<Boolean> = callbackFlow {
        val newParameters = findDifferences(old, new)
        db.document(documentPath).update(newParameters).addOnSuccessListener {
            trySend(true)
        }.addOnFailureListener {
            trySend(false)
        }
    }

    private fun <T: Any> findDifferences(old: T, new: T): Map<String, Any>{
        val kClass = old.javaClass.kotlin
        val ret = emptyMap<String, Any>().toMutableMap()
        kClass.declaredMemberProperties.forEach {
            val valueOld = it.get(old)
            val valueNew = it.get(new)
            if(valueNew != null && valueOld != null && valueNew != valueOld){
                ret[it.name] = valueNew
            }
        }
        return ret.toMap()
    }

    protected fun deleteDocument(documentPath: String): Flow<Boolean> = callbackFlow {
        db.document(documentPath).delete().addOnSuccessListener {
            trySend(true)
        }.addOnFailureListener {
            trySend(false)
        }
    }

    protected fun randomString(len: Int): String{
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return String(CharArray(len) { allowedChars.random() })
    }

    /** Returns the uri used to save the attachment */
    protected fun uploadAttachment(taskId: String, uri: Uri, name: String): Flow<String?> = callbackFlow {
        val randomString = randomString(16)
        val ref = storage.child("attachments/$taskId/${name}_$randomString")
        ref.putFile(uri).addOnSuccessListener {
            trySend("attachments/$taskId/${name}_$randomString")
        }.addOnFailureListener {
            trySend(null)
        }
    }

    protected fun removeAttachment(uri: Uri): Flow<Boolean> = callbackFlow {
        val ref = storage.child(uri.toString())
        ref.delete().addOnSuccessListener {
            trySend(true)
        }.addOnFailureListener {
            trySend(false)
        }
    }
}