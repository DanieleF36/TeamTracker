package it.application.team_tracker.model.daoes

import it.application.team_tracker.model.remoteDataSource.entities.Tag

interface TagDAO {
    fun getTags(): List<Tag>
    fun getTagsByName(name: String): List<Tag>
    fun getTagsByTask(taskId: String): List<Tag>
    fun addTag(tag: Tag)
    fun addTags(vararg tag: Tag)
    fun updateTag(tag: Tag)
    fun deleteTag(id: String)
}