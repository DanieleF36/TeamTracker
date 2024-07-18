package it.application.team_tracker.model.daoes

interface AttachmentDAO {
    fun getAttachment(id:String): Attachment
    fun getAttachmentsByTask(taskId: String): List<Attachment>
    fun addAttachment(attachment: Attachment)
    fun addAttachments(vararg attachments: Attachment)
    fun updateAttachment(attachment: Attachment)
    fun deleteAttachment(id: String)
    fun deleteAttachments(ids: List<String>)
}