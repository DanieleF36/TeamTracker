package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import it.application.team_tracker.model.localDataSource.entities.TaskComplete

@Dao
interface TaskCompleteDAO {
    @Transaction
    @Query("SELECT * FROM task WHERE id=:taskId")
    suspend fun getTask(taskId: String): TaskComplete
}