package com.livepokernotes.framework.datasource.network.abstraction

import com.livepokernotes.business.domain.model.Session

interface SessionFirestoreService {

    suspend fun insertOrUpdateSession(session: Session)
    suspend fun insertOrUpdateSessions(sessions: List<Session>)
    suspend fun deleteSession(primaryKey: String)
    suspend fun searchSessionById(primaryKey: String): Session?
    suspend fun deleteSessions(sessionList: List<Session>)
    suspend fun getAllSessions(): List<Session>
}