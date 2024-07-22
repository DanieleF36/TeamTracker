package it.application.team_tracker.model.daoes.remote

import it.application.team_tracker.model.entities.Message
import kotlinx.coroutines.flow.Flow

interface MessageDAO {
    /**
     * Returns the messages of the team chat or null if there are no messages
     * teamId
     * monthBehind: the first message returned will be sent in date dd/(mm-monthBehind)/yyyy
     */
    fun getTeamMessages(teamId: String, monthBehind: Int): Flow<Message?>
    /**
     * Returns a Pair the change linked to that massage and the message of the team chat or null if there are no messages
     * teamId
     * listenForUpdate
     * monthBehind: the first message returned will be sent in date dd/(mm-monthBehind)/yyyy
     */
    fun getTeamMessages(teamId: String, monthBehind: Int, listenForUpdate: Boolean = true): Flow<Pair<ChangeType, Message>?>
    /**
     * Returns the messages of the private chat between two person or null if there are no messages
     * userId
     * monthBehind: the first message returned will be sent in date dd/(mm-monthBehind)/yyyy
     */
    fun getPrivateMessages(userId: String, monthBehind: Int): Flow<Message?>
    /**
     * Returns the messages of the private chat between two person or null if there are no messages
     * userId
     * listenForUpdate
     * monthBehind: the first message returned will be sent in date dd/(mm-monthBehind)/yyyy
     */
    fun getPrivateMessages(userId: String, monthBehind: Int, listenForUpdate: Boolean = true): Flow<Pair<ChangeType, Message>?>
    /**
     * Add a new message to a team chat and returns it id
     */
    fun sendTeamMessage(teamId: String, message: Message): Flow<String?>
    /**
     * Add a new message to a private chat and returns it id
     */
    fun sendPrivateMessage(userId: String, message: Message): Flow<String?>

    fun setLastReadMessage(userId: String, messageId: String): Flow<Boolean>

    fun getUnreadTeamMessageCount(teamId: String): Flow<Int>

    fun getUnreadTeamMessageCount(teamId: String, listenForUpdates: Boolean = true): Flow<Int>

    fun getUnreadPrivateMessageCount(userId: String): Flow<Int>

    fun getUnreadPrivateMessageCount(userId: String, listenForUpdates: Boolean = true): Flow<Int>
}