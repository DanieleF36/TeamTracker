package it.application.team_tracker.model.daoes.remote

import it.application.team_tracker.model.entities.Attachment
import it.application.team_tracker.model.entities.Comment
import it.application.team_tracker.model.entities.History
import it.application.team_tracker.model.entities.Task
import kotlinx.coroutines.flow.Flow

interface TaskDAO {
    /**
     * returns a specific tasks or null if it does not exist
     */
    fun getTask(taskId: String, listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<Task?>
    /**
     * returns the tasks the user has been assigned to or null if no user has been assigned to it
     */
    fun getUserTasks(userId: String, listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<Task?>
    /**
     * returns the tasks the of the team or null if no task has been created
     */
    fun getTeamTasks(teamId: String, listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<Task>
    /**
     *  add a new task and return its id
     */
    fun addTask(taskId: String): Flow<String>

    fun updateTask(taskId: String): Flow<Boolean>

    fun removeUser(taskId: String, userId: String): Flow<Boolean>

    fun updateUserRole(userId: String, newRole: String): Flow<Boolean>
    /**
     * returns the comments the of the task or null if no comment has been created
     */
    fun getComments(taskId: String, listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<Comment?>
    /**
     * add a new comment and return its id
     */
    fun addComment(taskId: String, comment: Comment): Flow<String>
    /**
     * returns the attachments of the task or null if no attachment has been uploaded to it
     */
    fun getTaskAttachments(taskId: String, listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<Attachment?>
    /**
     * returns a specific attachment to or null if it does not exist
     */
    fun getAttachment(idAttachment: String, listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<Attachment?>
    /**
     * add a new attachment and return its id
     */
    fun addAttachment(attachment: Attachment): Flow<String>
    /**
     * returns the history of the task or null if no update have been done
     */
    fun getTaskHistory(taskId: String, listenForUpdates: Boolean = true, onUpdate: (ChangeType)->Unit): Flow<History?>
    //TODO aggiungere funzioni per i tag
}