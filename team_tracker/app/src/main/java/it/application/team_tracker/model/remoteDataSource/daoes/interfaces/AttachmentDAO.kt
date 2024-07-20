package it.application.team_tracker.model.remoteDataSource.daoes.interfaces

interface AttachmentDAO {
    fun getAttachment(id:String): AttachmentDAO
    fun getAttachmentsByTask(taskId: String): List<AttachmentDAO>
    fun addAttachment(attachment: AttachmentDAO)
    fun addAttachments(vararg attachments: AttachmentDAO)
    fun updateAttachment(attachment: AttachmentDAO)
    fun deleteAttachment(id: String)
    fun deleteAttachments(ids: List<String>)
}