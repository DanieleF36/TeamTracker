package it.application.team_tracker.model.modelsImpl

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import it.application.team_tracker.model.LocalDataSource
import it.application.team_tracker.model.RemoteDataSource
import it.application.team_tracker.model.TaskModel
import it.application.team_tracker.model.daoes.remote.ChangeType
import it.application.team_tracker.model.entities.Attachment
import it.application.team_tracker.model.entities.Comment
import it.application.team_tracker.model.entities.History
import it.application.team_tracker.model.entities.Task
import it.application.team_tracker.model.exception.InternetUnavailableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskModelImpl: TaskModel {
    //This are the task of a single team
    private val tasks = mutableStateListOf<Task?>(null)
    private val userTasks = mutableStateListOf<Task?>(null)

    private var getTeamTaskCoroutine: Job? = null
    private var getUserTaskCoroutine: Job? = null

    @Inject
    lateinit var local: LocalDataSource
    //assumption: in the absence of internet the RemoteDataSource caches the queries and will do them as soon as the internet is back
    @Inject
    lateinit var remote: RemoteDataSource

    override fun getTask(taskId: String): Flow<Task?> = flow{
        var ret = tasks.find { it?.id == taskId }
        if(ret != null)
            emit(ret)
        else{
            ret = userTasks.find { it?.id == taskId }
            if( ret != null)
                emit(ret)
            else{
                local.taskDao().getTask(taskId).collect{
                    if(it != null) {
                        emit(it)
                    }
                    else {
                        remote.taskDao().getTask(taskId).collect{ remoteTask ->
                            if(remoteTask != null){
                                local.taskDao().addTask(remoteTask)
                                emit(remoteTask)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getUserTasks(userId: String): List<Task?> {
        getUserTaskCoroutine?.cancel()
        userTasks.clear()
        getUserTaskCoroutine = CoroutineScope(Dispatchers.IO).launch {
            remote.taskDao().getUserTasksWithUpdate(userId).collect{
                if(it != null){
                    when(it.first){
                        ChangeType.ADD -> { userTasks.add(it.second); local.taskDao().addTask(it.second) }
                        ChangeType.REMOVE -> { userTasks.remove(it.second); local.taskDao().removeTask(it.second.id) }
                        ChangeType.UPDATE -> {
                            val index = userTasks.indexOfFirst { f -> f?.id==it.second.id }
                            local.taskDao().updateTask(userTasks[index]!!, it.second)
                            userTasks[index] = it.second
                        }
                    }

                }
            }
        }
        return userTasks.toList()
    }

    override fun getTeamTasks(teamId: String): List<Task?> {
        getTeamTaskCoroutine?.cancel()
        tasks.clear()
        getTeamTaskCoroutine = CoroutineScope(Dispatchers.IO).launch {
            remote.taskDao().getTeamTasksWithUpdate(teamId).collect{
                if(it != null){
                    when(it.first){
                        ChangeType.ADD -> { tasks.add(it.second); local.taskDao().addTask(it.second) }
                        ChangeType.REMOVE -> { tasks.remove(it.second); local.taskDao().removeTask(it.second.id) }
                        ChangeType.UPDATE -> {
                            val index = tasks.indexOfFirst { f -> f?.id==it.second.id }
                            local.taskDao().updateTask(tasks[index]!!, it.second)
                            tasks[index] = it.second
                        }
                    }

                }
            }
        }
        return tasks.toList()
    }

    override fun addTask(task: Task): Flow<String?> {
        return remote.taskDao().addTask(task).onEach {
            if (it != null){
                val new = task.copy(id = it)
                local.taskDao().addTask(new)
            }
        }
    }

    override fun updateTask(oldTask: Task, newTask: Task): Flow<Boolean> {
        return remote.taskDao().updateTask(oldTask, newTask).onCompletion {
            if (it == null || it is InternetUnavailableException)
                local.taskDao().updateTask(oldTask, newTask)
        }
    }

    override fun removeTask(taskId: String): Flow<Boolean> {
        return remote.taskDao().removeTask(taskId).onCompletion {
            if (it == null || it is InternetUnavailableException)
                local.taskDao().removeTask(taskId)
        }
    }

    override fun removeUser(taskId: String, userId: String): Flow<Boolean> {
        return remote.taskDao().removeUser(taskId, userId).onCompletion {
            if (it == null || it is InternetUnavailableException)
                local.taskDao().removeUser(taskId, userId)
        }
    }

    override fun updateUserRole(taskId: String, userId: String, newRole: String): Flow<Boolean> {
        return remote.taskDao().updateUserRole(taskId, userId, newRole).onCompletion {
            if(it == null || it is InternetUnavailableException)
                local.taskDao().updateUserRole(taskId, userId, newRole)
        }
    }

    override fun getComments(taskId: String): Flow<Comment?> {
        TODO("Not yet implemented")
    }

    override fun addComment(comment: Comment): Flow<String?> {
        return remote.taskDao().addComment(comment).onEach {
            if(it != null)
                local.taskDao().addComment(comment.copy(id = it))
        }
    }

    override fun getTaskAttachments(taskId: String): Flow<Attachment?> {
        TODO("Not yet implemented")
    }

    override fun downloadAttachment(context: Context, uri: Uri) {
        remote.taskDao().downloadAttachment(context, uri)
    }

    override fun addAttachment(attachment: Attachment): Flow<String?> {
        return remote.taskDao().addAttachment(attachment).onEach {
            if(it != null)
                local.taskDao().addAttachment(attachment.copy(id = it))
        }
    }

    override fun getTaskHistory(taskId: String): Flow<History?> {
        TODO("Not yet implemented")
    }

    override fun getTaskTags(taskId: String): Flow<String?> {
        TODO("Not yet implemented")
    }

    override fun addTaskTag(taskId: String, tag: String): Flow<String?> {
        return remote.taskDao().addTaskTag(taskId, tag).onCompletion {
            if(it == null || it is InternetUnavailableException)
                local.taskDao().addTaskTag(taskId, tag)
        }
    }

}