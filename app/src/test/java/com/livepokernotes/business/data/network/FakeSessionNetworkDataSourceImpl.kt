package com.livepokernotes.business.data.network

import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.domain.model.Session

class FakeSessionNetworkDataSourceImpl
constructor(
    private val sessionsData: HashMap<String, Session>,
    private val deletedSessionsData: HashMap<String, Session>
): SessionNetworkDataSource {
    override suspend fun insertSession(session: Session) {
        sessionsData[session.id] = session
    }

    override suspend fun deleteSession(primaryKey: String) {
        sessionsData.remove(primaryKey)
    }

    override suspend fun searchSessionById(primaryKey: String): Session?
        = sessionsData[primaryKey]


    override suspend fun deleteSessions(sessionList: List<Session>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSession(primaryKey: String, comment: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSessions(): List<Session> {
        TODO("Not yet implemented")
    }

}