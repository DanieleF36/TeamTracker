package it.application.team_tracker.model.remoteDataSource.daoes.interfaces

import it.application.team_tracker.model.remoteDataSource.entities.Message

interface MessageDAO {
    fun getDirectMessages(user1Id: String, user2Id: String): List<Message>
    fun getTeamMessages(teamId: String): List<Message>
    fun addMessage(message: Message)
    fun addMessages(vararg messages: Message)
    fun updateMessage(message: Message)
    fun deleteMessage(id: String)
    fun deleteDirectMessages(user1Id: String, user2Id: String)
    fun deleteTeamMessages(teamId: String)
}