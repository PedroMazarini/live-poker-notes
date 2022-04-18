package com.livepokernotes.framework.datasource.network.abstraction

import com.livepokernotes.business.domain.model.Session

interface SessionFirestoreService {

    suspend fun insertSession(session: Session)
    suspend fun deleteSession(primaryKey: String)
    suspend fun deleteSessions(sessionList: List<Session>)
    suspend fun updateSession(primaryKey: String, comment: String): Int
    suspend fun getAllSessions(): List<Session>
}