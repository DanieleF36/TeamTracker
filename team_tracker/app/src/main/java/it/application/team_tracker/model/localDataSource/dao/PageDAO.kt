package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.Page
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDAO {
    @Query("SELECT * FROM page WHERE team_id=:teamId")
    suspend fun getPagesByTeam(teamId: String): Flow<Page>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPage(new: Page)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPages(vararg new: Page)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updatePage(new: Page)
    @Query("DELETE FROM page WHERE id=:id")
    suspend fun deletePage(id: String)
    @Query("DELETE FROM page WHERE team_id=:teamId")
    suspend fun deletePages(teamId: String)
}