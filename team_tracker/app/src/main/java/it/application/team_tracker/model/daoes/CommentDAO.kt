package it.application.team_tracker.model.daoes

import it.application.team_tracker.model.entities.Comment
import kotlinx.coroutines.flow.Flow

interface CommentDAO {
    /**
     * returns the comments the of the task or null if no comment has been created
     */
    fun getTaskComments(taskId: String, listenForUpdates: Boolean = true): Flow<Comment?>
    /**
     * add a new comment and return its id
     */
    fun addComment(taskId: String): Flow<String>
}