package it.application.team_tracker.model.daoes.remote

import it.application.team_tracker.model.entities.Comment
import it.application.team_tracker.model.entities.Task
import kotlinx.coroutines.flow.Flow

interface TaskDAO {
    /**
     * returns a specific tasks or null if it does not exist
     */
    fun getTask(taskId: String, listenForUpdates: Boolean = true): Flow<Task?>
    /**
     * returns the tasks the user has been assigned to or null if no user has been assigned to it
     */
    fun getUserTasks(userId: String, listenForUpdates: Boolean = true): Flow<Task?>
    /**
     * returns the tasks the of the team or null if no task has been created
     */
    fun getTeamTasks(teamId: String, listenForUpdates: Boolean = true): Flow<Task>
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
    fun getComments(taskId: String, listenForUpdates: Boolean = true): Flow<Comment?>
    /**
     * add a new comment and return its id
     */
    fun addComment(taskId: String, comment: Comment): Flow<String>
}