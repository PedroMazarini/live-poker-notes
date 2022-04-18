package com.livepokernotes.business.data.network.implementation

import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.framework.datasource.network.abstraction.SessionFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionNetworkDataSourceImpl
@Inject
constructor(
    private val firestoreService: SessionFirestoreService
): SessionNetworkDataSource {
    override suspend fun insertSession(session: Session)
        = firestoreService.insertSession(session)

    override suspend fun deleteSession(primaryKey: String)
        = firestoreService.deleteSession(primaryKey)

    override suspend fun deleteSessions(sessionList: List<Session>)
        = firestoreService.deleteSessions(sessionList)

    override suspend fun updateSession(primaryKey: String, comment: String)
        = firestoreService.updateSession(primaryKey, comment)

    override suspend fun getAllSessions() = firestoreService.getAllSessions()

}