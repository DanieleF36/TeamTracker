package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Query("SELECT * FROM user WHERE id=:id")
    fun getUser(id: String): Flow<User?>
    @Query("SELECT * FROM user WHERE nickname=:nickname")
    fun getByNickname(nickname: String): User?
    @Query("SELECT * FROM user WHERE nickname LIKE :nickname")
    fun getLikeNickname(nickname: String): Flow<List<User>>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUser(user: User)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUsers(vararg user: User)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateUser(user: User)
    @Query("DELETE FROM user WHERE id=:id")
    suspend fun deleteUser(id: String)
}