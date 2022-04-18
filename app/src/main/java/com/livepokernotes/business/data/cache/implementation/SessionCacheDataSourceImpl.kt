package com.livepokernotes.business.data.cache.implementation

import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.framework.datasource.cache.abstraction.SessionDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionCacheDataSourceImpl
@Inject
constructor(
    private val sessionDaoService: SessionDaoService
): SessionCacheDataSource {
    override suspend fun insertSession(session: Session) =
        sessionDaoService.insertSession(session)

    override suspend fun deleteSession(primaryKey: String) =
        sessionDaoService.deleteSession(primaryKey)

    override suspend fun deleteSessions(sessionList: List<Session>) =
        sessionDaoService.deleteSessions(sessionList)

    override suspend fun updateSession(primaryKey: String, comment: String) =
        sessionDaoService.updateSession(primaryKey, comment)

    override suspend fun searchSessions(
        query: String,
        filterAndOrder: String,
        page: Int
    ) = sessionDaoService.searchSessions(query, filterAndOrder, page)

    override suspend fun searchSessionById(primaryKey: String) =
        sessionDaoService.searchSessionById(primaryKey)

}
