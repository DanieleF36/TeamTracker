package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.Feedback
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDAO {
    @Query("SELECT * FROM feedback WHERE user_id=:userId")
    suspend fun getFeedbackByUser(userId: String): Flow<Feedback>
    @Query("SELECT * FROM feedback WHERE team_id=:teamId")
    suspend fun getFeedbackByTeam(teamId: String): Flow<Feedback>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFeedback(new: Feedback)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFeedbacks(vararg new: Feedback)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateFeedback(new: Feedback)
    @Query("DELETE FROM feedback WHERE id=:id")
    suspend fun deleteFeedback(id: String)
    @Query("DELETE FROM feedback WHERE id IN (:ids)")
    suspend fun deleteFeedbacks(ids: List<String>)
}