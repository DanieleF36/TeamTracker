package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDAO {
    @Query("SELECT * FROM history WHERE task_id=:taskId")
    suspend fun getHistoriesByTask(taskId: String): Flow<History>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addHistory(new: History)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addHistories(vararg new: History)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateHistory(new: History)
    @Query("DELETE FROM history WHERE id=:id")
    suspend fun deleteHistory(id: String)
    @Query("DELETE FROM history WHERE id IN (:ids)")
    suspend fun deleteHistories(ids: List<String>)
}