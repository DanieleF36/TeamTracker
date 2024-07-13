package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDAO {
    @Query("SELECT * FROM comment WHERE task_id=:taskId")
    suspend fun getCommentsByTask(taskId: String): List<Comment>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addComment(comment: Comment)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addComments(vararg comments: Comment)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateComment(comment: Comment)
    @Query("DELETE FROM comment WHERE id=:id")
    suspend fun deleteAttachment(id: String)
    @Query("DELETE FROM comment WHERE id IN (:ids)")
    suspend fun deleteAttachments(ids: List<String>)
}