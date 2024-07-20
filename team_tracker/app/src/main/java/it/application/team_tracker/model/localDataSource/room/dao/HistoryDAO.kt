package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDAO {
    @Query("SELECT * FROM history WHERE task_id=:taskId")
    fun getHistoriesByTask(taskId: String): Flow<History?>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addHistory(history: History)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addHistories(vararg histories: History)
    @Update(onConflict = OnConflictStrategy.ABORT)
    fun updateHistory(history: History)
    @Query("DELETE FROM history WHERE id=:id")
    fun deleteHistory(id: String)
    @Query("DELETE FROM history WHERE id IN (:ids)")
    fun deleteHistories(ids: List<String>)
}