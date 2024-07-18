package it.application.team_tracker.model.daoes


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