package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.Kpi
import kotlinx.coroutines.flow.Flow

@Dao
interface KpiDAO {
    @Query("SELECT * FROM kpi WHERE user_id=:userId")
    suspend fun getKpisByUser(userId: String): List<Kpi>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addKpi(kpi: Kpi)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addKpis(vararg kpis: Kpi)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateKpi(kpi: Kpi)
    @Query("DELETE FROM kpi WHERE id=:id")
    suspend fun deleteKpi(id: String)
    @Query("DELETE FROM kpi WHERE id IN (:ids)")
    suspend fun deleteKpis(ids: List<String>)
}