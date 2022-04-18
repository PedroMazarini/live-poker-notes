package com.livepokernotes.business.data.network.abstraction

import com.livepokernotes.business.domain.model.Session

interface SessionNetworkDataSource {

    suspend fun insertSession(session: Session)
    suspend fun deleteSession(primaryKey: String)
    suspend fun deleteSessions(sessionList: List<Session>)
    suspend fun updateSession(primaryKey: String, comment: String): Int
    suspend fun getAllSessions(): List<Session>

}