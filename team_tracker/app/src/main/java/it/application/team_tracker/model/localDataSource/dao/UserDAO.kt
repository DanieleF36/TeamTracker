package it.application.team_tracker.model.localDataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.entities.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM user WHERE id=:id")
    suspend fun getUser(id: String): User
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUser(user: User)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUsers(vararg user: User)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateUser(user: User)
    @Query("DELETE FROM user WHERE id=:id")
    suspend fun deleteUser(id: String)
}