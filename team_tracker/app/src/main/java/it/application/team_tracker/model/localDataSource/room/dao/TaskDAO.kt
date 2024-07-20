package it.application.team_tracker.model.localDataSource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.application.team_tracker.model.localDataSource.room.entities.Task

@Dao
interface TaskDAO {
    @Query("SELECT * FROM task WHERE id=:id")
    suspend fun getTask(id: String): Task
    @Query("SELECT * FROM task WHERE team_id=:idTeam")
    suspend fun getTasksByTeam(idTeam: String): List<Task>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTask(task: Task)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTasks(vararg task: Task)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateTask(task: Task)
    @Query("DELETE FROM task WHERE id=:id")
    suspend fun deleteTask(id: String)
    @Query("DELETE FROM task WHERE team_id=:teamId")
    suspend fun deleteTasks(teamId: String)
}