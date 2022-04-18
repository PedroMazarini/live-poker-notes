package com.livepokernotes.business.data.cache.abstraction

import com.livepokernotes.business.domain.model.Session

interface SessionCacheDataSource {

    suspend fun insertSession(session: Session): Long
    suspend fun deleteSession(primaryKey: String): Int
    suspend fun deleteSessions(sessionList: List<Session>): Int
    suspend fun updateSession(primaryKey: String, comment: String): Int
    suspend fun searchSessions(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Session>
    suspend fun searchSessionById(primaryKey: String): Session?
}