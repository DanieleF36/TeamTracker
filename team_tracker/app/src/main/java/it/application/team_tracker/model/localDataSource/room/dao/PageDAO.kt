package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.Page
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDAO {
    @Query("SELECT * FROM page WHERE team_id=:teamId")
    suspend fun getPagesByTeam(teamId: String): List<Page>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPage(page: Page)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPages(vararg pages: Page)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updatePage(page: Page)
    @Query("DELETE FROM page WHERE id=:id")
    suspend fun deletePage(id: String)
    @Query("DELETE FROM page WHERE team_id=:teamId")
    suspend fun deletePages(teamId: String)
}