package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDAO {
    @Query("SELECT * FROM comment WHERE task_id=:taskId")
    fun getCommentsByTask(taskId: String): Flow<Comment?>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addComment(comment: Comment)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addComments(vararg comments: Comment)
    @Update(onConflict = OnConflictStrategy.ABORT)
    fun updateComment(comment: Comment)
    @Query("DELETE FROM comment WHERE id=:id")
    fun deleteAttachment(id: String)
    @Query("DELETE FROM comment WHERE id IN (:ids)")
    fun deleteAttachments(ids: List<String>)
}