package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.Kpi
import kotlinx.coroutines.flow.Flow

@Dao
interface KpiDAO {
    @Query("SELECT * FROM kpi WHERE user_id=:userId")
    suspend fun getKpisByUser(userId: String): Flow<Kpi>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addKpi(new: Kpi)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addKpis(vararg new: Kpi)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateKpi(new: Kpi)
    @Query("DELETE FROM kpi WHERE id=:id")
    suspend fun deleteKpi(id: String)
    @Query("DELETE FROM kpi WHERE id IN (:ids)")
    suspend fun deleteKpis(ids: List<String>)
}