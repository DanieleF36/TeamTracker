package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.Feedback

@Dao
interface FeedbackDAO {
    @Query("SELECT * FROM feedback WHERE user_id=:userId")
    suspend fun getFeedbacksByUser(userId: String): List<Feedback>
    @Query("SELECT * FROM feedback WHERE team_id=:teamId")
    suspend fun getFeedbacksByTeam(teamId: String): List<Feedback>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFeedback(feedback: Feedback)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFeedbacks(vararg feedbacks: Feedback)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateFeedback(feedback: Feedback)
    @Query("DELETE FROM feedback WHERE id=:id")
    suspend fun deleteFeedback(id: String)
    @Query("DELETE FROM feedback WHERE id IN (:ids)")
    suspend fun deleteFeedbacks(ids: List<String>)
}