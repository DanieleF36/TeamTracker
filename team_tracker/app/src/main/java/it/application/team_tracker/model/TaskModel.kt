package it.application.team_tracker.model

import android.content.Context
import android.net.Uri
import it.application.team_tracker.model.entities.Attachment
import it.application.team_tracker.model.entities.Comment
import it.application.team_tracker.model.entities.History
import it.application.team_tracker.model.entities.Task
import kotlinx.coroutines.flow.Flow

interface TaskModel {
    fun getTask(taskId: String): Flow<Task?>
    fun getUserTasks(userId: String): List<Task?>
    fun getTeamTasks(teamId: String): List<Task?>
    fun addTask(task: Task): Flow<String?>
    fun updateTask(oldTask: Task, newTask: Task): Flow<Boolean>
    fun removeTask(taskId: String): Flow<Boolean>
    fun removeUser(taskId: String, userId: String): Flow<Boolean>
    fun updateUserRole(taskId: String, userId: String, newRole: String): Flow<Boolean>
    fun getComments(taskId: String): Flow<Comment?>
    fun addComment(comment: Comment): Flow<String?>
    fun getTaskAttachments(taskId: String): Flow<Attachment?>
    fun downloadAttachment(context: Context, uri: Uri)
    fun addAttachment(attachment: Attachment): Flow<String?>
    fun getTaskHistory(taskId: String): Flow<History?>
    fun getTaskTags(taskId: String): Flow<String?>
    fun addTaskTag(taskId: String, tag: String): Flow<String?>
}