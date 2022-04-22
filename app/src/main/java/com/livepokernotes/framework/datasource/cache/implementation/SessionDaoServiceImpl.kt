package com.livepokernotes.framework.datasource.cache.implementation

import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.util.DateUtil
import com.livepokernotes.framework.datasource.cache.abstraction.SessionDaoService
import com.livepokernotes.framework.datasource.cache.mappers.CacheMapper.mapToEntity
import com.livepokernotes.framework.datasource.cache.mappers.CacheMapper.mapToDomainList
import com.livepokernotes.framework.datasource.cache.mappers.CacheMapper.mapToDomainModel
import com.livepokernotes.framework.datasource.database.SessionDao
import javax.inject.Inject

class SessionDaoServiceImpl
@Inject
constructor(
    private val sessionDao: SessionDao,
    private val dateUtil: DateUtil
) : SessionDaoService {

    override suspend fun insertSession(session: Session): Long =
        sessionDao.insertSession(session.mapToEntity())

    override suspend fun deleteSession(primaryKey: String): Int =
        sessionDao.deleteSession(primaryKey)

    override suspend fun deleteSessions(sessionList: List<Session>): Int =
        sessionDao.deleteSessions(sessionList.map { it.id })

    override suspend fun updateSession(primaryKey: String, comment: String): Int =
        sessionDao.updateSession(primaryKey, comment, dateUtil.getCurrentDate())

    override suspend fun searchSessions(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Session> = sessionDao.searchSessions(
        query = query,
        filterAndOrder = filterAndOrder,
        page = page
    ).mapToDomainList()

    override suspend fun searchSessionById(primaryKey: String): Session? =
        sessionDao.searchSessionById(primaryKey)?.mapToDomainModel()

    override suspend fun getNumSessions(): Int =
        sessionDao.getNumSessions()

}