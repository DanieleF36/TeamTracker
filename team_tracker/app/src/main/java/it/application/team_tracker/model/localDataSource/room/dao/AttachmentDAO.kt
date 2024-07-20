package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.Attachment
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDAO {
    @Query("SELECT * FROM attachment WHERE id=:id")
    fun getAttachment(id:String): Flow<Attachment?>
    @Query("SELECT * FROM attachment WHERE task_id=:taskId")
    fun getAttachmentsByTask(taskId: String): Flow<Attachment?>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addAttachment(attachment: Attachment)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addAttachments(vararg attachments: Attachment)
    @Update(onConflict = OnConflictStrategy.ABORT)
    fun updateAttachment(attachment: Attachment)
    @Query("DELETE FROM attachment WHERE id=:id")
    fun deleteAttachment(id: String)
    @Query("DELETE FROM attachment WHERE id IN (:ids)")
    fun deleteAttachments(ids: List<String>)
}