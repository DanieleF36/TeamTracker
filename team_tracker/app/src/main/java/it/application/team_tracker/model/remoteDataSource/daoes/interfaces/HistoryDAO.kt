package it.application.team_tracker.model.remoteDataSource.daoes.interfaces

import it.application.team_tracker.model.remoteDataSource.entities.History

interface HistoryDAO {
    fun getHistoriesByTask(taskId: String): List<History>
    fun addHistory(history: History)
    fun addHistories(vararg histories: History)
    fun updateHistory(history: History)
    fun deleteHistory(id: String)
    fun deleteHistories(ids: List<String>)
}