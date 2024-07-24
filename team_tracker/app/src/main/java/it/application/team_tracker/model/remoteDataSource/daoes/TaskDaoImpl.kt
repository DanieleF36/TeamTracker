package it.application.team_tracker.model.remoteDataSource.daoes

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startForegroundService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.daoes.remote.TaskDAO
import it.application.team_tracker.model.entities.Attachment
import it.application.team_tracker.model.entities.Comment
import it.application.team_tracker.model.entities.History
import it.application.team_tracker.model.entities.Task
import it.application.team_tracker.model.remoteDataSource.DownloadService
import it.application.team_tracker.model.remoteDataSource.entities.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class TaskDaoImpl:FirebaseDAO(), TaskDAO {
    override fun getTask(taskId: String): Flow<Task?> {
        return getDocument<it.application.team_tracker.model.remoteDataSource.entities.Task>("/tasks/$taskId").map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getTaskWithUpdate(taskId: String): Flow<Pair<ChangeType, Task>?> {
        return getDocumentWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Task>("/tasks/$taskId").map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun getUserTasks(userId: String): Flow<Task?> {
        val query = db.collection("/tasks").whereArrayContains("members", userId)
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Task>(query).map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getUserTasksWithUpdate(userId: String): Flow<Pair<ChangeType, Task>?> {
        val query = db.collection("/taks").whereArrayContains("members", userId)
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Task>(query).map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun getTeamTasks(teamId: String): Flow<Task?> {
        val query = db.collection("/tasks").whereEqualTo("teamId", teamId)
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Task>(query).map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getTeamTasksWithUpdate(teamId: String): Flow<Pair<ChangeType, Task>?> {
        val query = db.collection("/tasks").whereEqualTo("teamId", teamId)
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Task>(query).map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun addTask(task: Task): Flow<String?> {
        return addDocument("/tasks", fromNeutralToRemote(task))
    }

    override fun updateTask(oldTask: Task, newTask: Task): Flow<Boolean> {
        return updateDocument("/tasks/${newTask.id}", fromNeutralToRemote(oldTask), fromNeutralToRemote(newTask))
    }

    override fun removeTask(taskId: String): Flow<Boolean> {
        return deleteDocument("/tasks/$taskId")
    }

    override fun removeUser(taskId: String, userId: String): Flow<Boolean> = callbackFlow{
        val batch = db.batch()
        batch.update(db.document("/tasks/$taskId"), "members", FieldValue.arrayRemove(userId))
        batch.update(db.document("/tasks/$taskId"),"membersAndRole.$userId", FieldValue.delete())
        batch.commit().addOnSuccessListener {
            trySend(true)
        }.addOnFailureListener {
            trySend(false)
        }
    }

    override fun updateUserRole(taskId: String, userId: String, newRole: String): Flow<Boolean> = callbackFlow{
        val batch = db.batch()
        batch.update(db.document("/tasks/$taskId"),"membersAndRole.$userId", FieldValue.delete())
        batch.update(db.document("/tasks/$taskId"),"membersAndRole.$userId", newRole)
        batch.commit().addOnSuccessListener {
            trySend(true)
        }.addOnFailureListener {
            trySend(false)
        }
    }

    override fun getComments(taskId: String): Flow<Comment?> {
        val query = db.collection("/comments").whereEqualTo("taskId", taskId)
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Comment>(query).map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getCommentsWithUpdate(taskId: String): Flow<Pair<ChangeType, Comment>?> {
        val query = db.collection("/comments").whereEqualTo("taskId", taskId)
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Comment>(query).map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun addComment(comment: Comment): Flow<String?> {
        return addDocument("/comments", fromNeutralToRemote(comment))
    }

    override fun getTaskAttachments(taskId: String): Flow<Attachment?> {
        val query = db.collection("/attachments").whereEqualTo("taskId", taskId)
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.Attachment>(query).map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getTaskAttachmentsWithUpdate(taskId: String): Flow<Pair<ChangeType, Attachment>?> {
        val query = db.collection("/attachments").whereEqualTo("taskId", taskId)
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.Attachment>(query).map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun downloadAttachment(context: Context, uri: Uri) {
        val intent = Intent(context, DownloadService::class.java)
        startForegroundService(context, intent)
    }

    override fun addAttachment(attachment: Attachment): Flow<String?> = callbackFlow{
        uploadAttachment(attachment.taskId, attachment.url, attachment.url.lastPathSegment!!).collect { uri ->
            if(uri != null)
                addDocument("/attachments", fromNeutralToRemote(attachment)).collect { id ->
                    if(id != null)
                        trySend(id)
                    else {
                        removeAttachment(Uri.parse(uri))
                        trySend(null)
                    }
                }

        }

    }

    override fun getTaskHistory(taskId: String): Flow<History?> {
        val query = db.collection("/histories").whereEqualTo("taskId", taskId)
        return getCollection<it.application.team_tracker.model.remoteDataSource.entities.History>(query).map {
            if(it != null)
                fromRemoteToNeutral(it)
            else
                null
        }
    }

    override fun getTaskHistoryWithUpdate(taskId: String): Flow<Pair<ChangeType, History>?> {
        val query = db.collection("/histories").whereEqualTo("taskId", taskId)
        return getCollectionWithUpdate<it.application.team_tracker.model.remoteDataSource.entities.History>(query).map {
            if(it != null)
                Pair(it.first, fromRemoteToNeutral(it.second))
            else
                null
        }
    }

    override fun getTaskTags(taskId: String): Flow<String?> {
        val query = db.collection("/tags").whereArrayContains("tasksId", taskId)
        return getCollection<Tag>(query).map { it?.name }
    }

    override fun getTaskTagsWithUpdate(taskId: String): Flow<Pair<ChangeType, String>?> {
        val query = db.collection("/tags").whereArrayContains("tasksId", taskId)
        return getCollectionWithUpdate<Tag>(query).map {
            if( it != null)
                Pair(it.first, it.second.name)
            else
                null
        }
    }

    override fun addTaskTag(taskId: String, tag: String): Flow<String?> {
        return addDocument("/tags", Tag("id", tag, listOf(taskId)))
    }

    private fun fromRemoteToNeutral(t: it.application.team_tracker.model.remoteDataSource.entities.Task): Task{
        return Task(
            t.id,
            t.name,
            t.description,
            Calendar.getInstance().apply { timeInMillis = t.dueDate.seconds },
            Calendar.getInstance().apply { timeInMillis = t.creationDate.seconds },
            Calendar.getInstance().apply { timeInMillis = t.closingDate.seconds },
            t.state,
            t.creator,
            t.closer,
            t.priority,
            t.teamId,
            t.timeSpent,
            t.membersAndRole,
            t.tags
        )
    }

    private fun fromNeutralToRemote(t: Task): it.application.team_tracker.model.remoteDataSource.entities.Task{
        return it.application.team_tracker.model.remoteDataSource.entities.Task(
            t.id,
            t.name,
            t.description,
            Timestamp(t.dueDate.time),
            Timestamp(t.creationDate.time),
            Timestamp(t.closingDate.time),
            t.state,
            t.creator,
            t.closer,
            t.priority,
            t.teamId,
            t.timeSpent,
            t.taskMembers,
            t.taskMembers.keys.toList(),
            t.tags
        )
    }

    private fun fromRemoteToNeutral(c: it.application.team_tracker.model.remoteDataSource.entities.Comment): Comment{
        return Comment(
            c.id,
            c.message,
            Calendar.getInstance().apply { timeInMillis = c.date.seconds },
            c.taskId,
            c.userId
        )
    }

    private fun fromNeutralToRemote(c: Comment): it.application.team_tracker.model.remoteDataSource.entities.Comment{
        return it.application.team_tracker.model.remoteDataSource.entities.Comment(
            c.id,
            c.message,
            Timestamp(c.date.time),
            c.taskId,
            c.userId
        )
    }

    private fun fromRemoteToNeutral(a: it.application.team_tracker.model.remoteDataSource.entities.Attachment): Attachment{
        return Attachment(
            a.id,
            Uri.parse(a.url),
            a.creator,
            Calendar.getInstance().apply { timeInMillis = a.date.seconds },
            a.taskId,
            a.userId
        )
    }

    private fun fromNeutralToRemote(a: Attachment): it.application.team_tracker.model.remoteDataSource.entities.Attachment{
        return it.application.team_tracker.model.remoteDataSource.entities.Attachment(
            a.id,
            a.url.toString(),
            a.creator,
            Timestamp(a.date.time),
            a.taskId,
            a.userId
        )
    }

    private fun fromRemoteToNeutral(h: it.application.team_tracker.model.remoteDataSource.entities.History): History{
        return History(
            h.id,
            h.type,
            h.message,
            Calendar.getInstance().apply { timeInMillis = h.date.seconds },
            h.taskId
        )
    }

}