package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDAO {
    @Query("SELECT * FROM message WHERE sender=:user1Id AND receiver=:user2Id")
    suspend fun getDirectMessages(user1Id: String, user2Id: String): List<Message>
    @Query("SELECT * FROM message WHERE team_id=:teamId")
    suspend fun getTeamMessages(teamId: String): List<Message>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMessage(message: Message)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMessages(vararg messages: Message)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateMessage(message: Message)
    @Query("DELETE FROM message WHERE id=:id")
    suspend fun deleteMessage(id: String)
    @Query("DELETE FROM message WHERE sender=:user1Id AND receiver=:user2Id")
    suspend fun deleteDirectMessages(user1Id: String, user2Id: String)
    @Query("DELETE FROM message WHERE team_id=:teamId")
    suspend fun deleteTeamMessages(teamId: String)
}