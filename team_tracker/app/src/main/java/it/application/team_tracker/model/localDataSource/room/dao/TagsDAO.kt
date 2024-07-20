package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.application.team_tracker.model.localDataSource.room.entities.Tags

@Dao
interface TagsDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTags(tags: Tags)
    @Query("DELETE FROM tags WHERE task_id=:idTask AND tag_id=:idTag")
    suspend fun deleteTags(idTask: String, idTag: String)
}