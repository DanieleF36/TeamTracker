package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.Attachment
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDAO {
    @Query("SELECT * FROM attachment WHERE id=:id")
    suspend fun getAttachment(id:String): Attachment
    @Query("SELECT * FROM attachment WHERE task_id=:taskId")
    suspend fun getAttachmentsByTask(taskId: String): List<Attachment>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAttachment(attachment: Attachment)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAttachments(vararg attachments: Attachment)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateAttachment(attachment: Attachment)
    @Query("DELETE FROM attachment WHERE id=:id")
    suspend fun deleteAttachment(id: String)
    @Query("DELETE FROM attachment WHERE id IN (:ids)")
    suspend fun deleteAttachments(ids: List<String>)
}