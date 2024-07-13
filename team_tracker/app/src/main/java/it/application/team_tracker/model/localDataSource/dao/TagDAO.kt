package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.Tag
import it.application.team_tracker.model.localDataSource.entities.Tags
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDAO {
    @Query("SELECT * FROM tag")
    suspend fun getTags(): Flow<Tag>
    @Query("SELECT * FROM tag WHERE name=:name")
    suspend fun getTagsByName(name: String): Flow<Tag>
    @Query("SELECT * FROM tag WHERE id IN (SELECT task_id FROM tags WHERE task_id=:taskId)")
    suspend fun getTagsByTask(taskId: String): Flow<Tag>
    /**
     * add a tags not linked with a task
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addGenericTag(newTag: Tag)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTagToTask(tags: Tags)
    @Transaction
    suspend fun addNewTagToTask(tag: Tag, tags: Tags){
        addGenericTag(tag)
        addTagToTask(tags)
    }
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTags(vararg new: Tags)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTags(vararg new: Tag)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateTag(new: Tag)
    @Query("DELETE FROM tag WHERE id=:id")
    suspend fun deleteTag(id: String)
}