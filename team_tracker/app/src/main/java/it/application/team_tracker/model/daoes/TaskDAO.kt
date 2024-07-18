package it.application.team_tracker.model.daoes


interface TaskDAO {
    fun getTask(id: String): Task
    fun getTasksByTeam(idTeam: String): List<Task>
    fun addTask(task: Task)
    fun addTasks(vararg task: Task)
    fun updateTask(task: Task)
    fun deleteTask(id: String)
    fun deleteTasks(teamId: String)
}