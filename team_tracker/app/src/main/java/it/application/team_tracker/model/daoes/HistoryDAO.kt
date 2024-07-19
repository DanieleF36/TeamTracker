package it.application.team_tracker.model.daoes

import it.application.team_tracker.model.entities.History
import kotlinx.coroutines.flow.Flow

interface HistoryDAO {
    /**
     * returns the history of the task or null if no update have been done
     */
    fun getTaskHistory(taskId: String, listenForUpdates: Boolean = true): Flow<History?>
}