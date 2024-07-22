package it.application.team_tracker.model.remoteDataSource.daoes

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query
import it.application.team_tracker.model.daoes.remote.ChangeType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

abstract class FirebaseDAO {
    protected val db = Firebase.firestore

    protected fun toChangeType(change: DocumentChange): ChangeType {
        return when(change.type) {
            DocumentChange.Type.ADDED -> ChangeType.ADD
            DocumentChange.Type.MODIFIED -> ChangeType.UPDATE
            DocumentChange.Type.REMOVED -> ChangeType.REMOVE
        }
    }

    protected inline fun <reified T> getCollection(
        query: Query,
        listenForUpdates: Boolean = true,
        crossinline onUpdate: (ChangeType, T)->Unit
    ): Flow<T?> = callbackFlow {
        val listener = if (listenForUpdates) {
            query.addSnapshotListener{ value, err ->
                if(err != null) {
                    TODO()
                }else {
                    if(value != null) {
                        val changes = value.documentChanges
                        value.documentChanges
                        for (i in 0..value.documents.size) {
                            val o = value.documents[i].toObject(T::class.java)
                            onUpdate(toChangeType(changes[i]), o!!)
                            trySend(o)
                        }
                    }
                    else
                        trySend(null)
                }
            }
        } else {
            query.get().addOnSuccessListener { r ->
                body(r, null)
                //close()
            }.addOnFailureListener {
                body(null, null)
            }
            null
        }
        awaitClose { listener?.remove() }
    }

    fun <T> getDocument(
        documentPath: String,
        listenForUpdates: Boolean = true,
        onDeserialize: (DocumentSnapshot) -> T
    ): Flow<LoadingStatus<T>> = callbackFlow {
        val body = { r: DocumentSnapshot?, _: FirebaseFirestoreException? ->
            if (r != null && r.data != null) {
                try {
                    val item = onDeserialize(r)
                    trySend(LoadingStatus.Done(item))
                } catch (e: Throwable) {
                    trySend(LoadingStatus.Error(e))
                }
            } else {
                trySend(LoadingStatus.Error("No such document"))
            }
            Unit
        }
        if (listenForUpdates) {
            //val listener = db.document(documentPath).addSnapshotListener(body)
            val listener = db.document(documentPath).addSnapshotListener(body)
            awaitClose { listener.remove() }
        } else {
            db.document(documentPath).get().addOnSuccessListener { r ->
                body(r, null)
                close()
            }.addOnFailureListener {
                // FIXME: better error handling
            }
            awaitClose()
        }
    }

    /**
     * Sets a document or creates a new one. If the provided `obj` implements [MapSerializable]'s
     * `id`, or if the `documentId` parameter is provided, an object with that `id` is going
     * to be created/set. If both are non-null, `documentId` takes precedence. If both are
     * null, a random id is going to be used.
     *
     * @param collectionPath a String
     * @param documentId a String, or null
     * @return a Flow<Boolean> that emits `true` in case of success or `false` in case of error
     * @see MapSerializable
     */
    fun <T : MapSerializable> setDocument(
        collectionPath: String,
        documentId: String? = null,
        obj: T
    ): Flow<String?> = callbackFlow {
        val map = obj.serializeToMap()
        val id = obj.id
        val coll = db.collection(collectionPath)

        val docRef = if (documentId == null && id == null) {
            coll.document()
        } else if (documentId != null) {
            coll.document(documentId)
        } else /* if (id != null) { */ {
            coll.document(id!!) // At this point id != null is necessarily true
        }

        docRef.set(map).addOnSuccessListener {
            trySend(docRef.id)
        }.addOnFailureListener {
            trySend(null)
        }
        awaitClose {}
    }

    /**
     * Updates an already existing document. The `map` argument can be constructed by hand, but
     * in most cases it is recommended to use the [objectDiff] function instead.
     *
     * @param documentPath a String
     * @param map a `Map` representing the fields to update
     * @return a Flow<Boolean> that emits `true` in case of success or `false` in case of error
     * @see objectDiff
     */
    fun updateDocument(
        documentPath: String,
        map: Map<String, Any?>
    ): Flow<Boolean> =
        callbackFlow {
            db.document(documentPath).update(map).addOnSuccessListener {
                trySend(true)
            }.addOnFailureListener {
                trySend(false)
            }
            awaitClose {}
        }
}
}