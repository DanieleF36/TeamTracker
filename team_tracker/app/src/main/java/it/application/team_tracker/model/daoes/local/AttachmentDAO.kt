package it.application.team_tracker.model.daoes.local

import it.application.team_tracker.model.entities.Attachment
import kotlinx.coroutines.flow.Flow

interface AttachmentDAO {
    /**
     * returns the attachments of the task or null if no attachment has been uploaded to it
     */
    fun getTaskAttachments(taskId: String, listenForUpdates: Boolean = true): Flow<Attachment?>
    /**
     * returns a specific attachment to or null if it does not exist
     */
    fun getAttachment(idAttachment: String, listenForUpdates: Boolean = true): Flow<Attachment?>

    /**
     * add a new attachment and return its id
     */
    fun addAttachment(attachment: Attachment): Flow<String>
}