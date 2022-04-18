package com.livepokernotes.business.data.cache

import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.business.domain.util.DateUtil

const val FORCE_DELETE_SESSION_EXCEPTION = "FORCE_DELETE_SESSION_EXCEPTION"
const val FORCE_UPDATE_SESSION_EXCEPTION = "FORCE_UPDATE_SESSION_EXCEPTION"
const val FORCE_NEW_SESSION_EXCEPTION = "FORCE_NEW_SESSION_EXCEPTION"
const val FORCE_SEARCH_SESSIONS_EXCEPTION = "FORCE_SEARCH_SESSIONS_EXCEPTION"
const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

class FakeSessionCacheDataSourceImpl
constructor(
    private val sessionsData: HashMap<String, Session>,
    private val dateUtil: DateUtil
): SessionCacheDataSource {

    override suspend fun insertSession(session: Session): Long {
        if(session.id.equals(FORCE_NEW_SESSION_EXCEPTION))
            throw Exception("Something went wrong inserting the session.")
        if(session.id.equals(FORCE_GENERAL_FAILURE))
            return -1 // failure
        sessionsData.put(session.id, session)
        return 1 //success
    }

    override suspend fun deleteSession(primaryKey: String): Int {
        if(primaryKey.equals(FORCE_DELETE_SESSION_EXCEPTION))
            throw Exception("Something went wrong deleting the session.")
        return sessionsData.remove(primaryKey)?.let {
            1 // success
        }?: -1 // failure
    }

    override suspend fun deleteSessions(sessionList: List<Session>): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updateSession(primaryKey: String, comment: String): Int {
        if(primaryKey.equals(FORCE_UPDATE_SESSION_EXCEPTION))
            throw Exception("Something went wrong updating the session")

        val updateSession = SessionFactory(dateUtil).createSingleSession(
            id = primaryKey,
            comment = comment,
            created_at = sessionsData[primaryKey]?.created_at
        )
        return sessionsData[primaryKey]?.let {
            sessionsData[primaryKey] = updateSession
            1 // success
        }?: -1 // failed, nothing to update
    }

    override suspend fun searchSessions(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Session> {
        TODO("Not yet implemented")
    }

    override suspend fun searchSessionById(primaryKey: String): Session? {
        TODO("Not yet implemented")
    }

}