package it.application.team_tracker.model.daoes

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
}