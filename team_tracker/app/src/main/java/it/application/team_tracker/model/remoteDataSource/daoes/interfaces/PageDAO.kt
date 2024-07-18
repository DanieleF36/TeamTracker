package it.application.team_tracker.model.remoteDataSource.daoes.interfaces

import it.application.team_tracker.model.remoteDataSource.entities.Page

interface PageDAO {
    fun getPagesByTeam(teamId: String): List<Page>
    fun addPage(page: Page)
    fun addPages(vararg pages: Page)
    fun updatePage(page: Page)
    fun deletePage(id: String)
    fun deletePages(teamId: String)
}