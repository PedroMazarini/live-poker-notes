package com.livepokernotes.business.interactors.sessionList

import com.livepokernotes.business.data.cache.CacheErrors
import com.livepokernotes.business.data.cache.FORCE_GENERAL_FAILURE
import com.livepokernotes.business.data.cache.FORCE_NEW_SESSION_EXCEPTION
import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.business.domain.state.DataState
import com.livepokernotes.business.interactors.sessionlist.InsertNewSession
import com.livepokernotes.business.interactors.sessionlist.InsertNewSession.Companion.INSERT_SESSION_FAILED
import com.livepokernotes.business.interactors.sessionlist.InsertNewSession.Companion.INSERT_SESSION_SUCCESS
import com.livepokernotes.di.DependencyContainer
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListStateEvent
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListViewState
import com.livepokernotes.util.isUnitTest
import junit.framework.Assert.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.*

class InsertNewSessionTest {

    //system in test
    private val insertNewSession: InsertNewSession

    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val sessionCacheDataSource: SessionCacheDataSource
    private val sessionNetworkDataSource: SessionNetworkDataSource
    private val sessionFactory: SessionFactory

    init {
        isUnitTest = true
        dependencyContainer.build()
        sessionCacheDataSource = dependencyContainer.sessionCacheDataSource
        sessionNetworkDataSource = dependencyContainer.sessionNetworkDataSource
        sessionFactory = dependencyContainer.sessionFactory
        insertNewSession = InsertNewSession(
            sessionCacheDataSource = sessionCacheDataSource,
            sessionNetworkDataSource = sessionNetworkDataSource,
            sessionFactory = sessionFactory
        )
    }

    @InternalCoroutinesApi
    @Test
    fun `Insert new session Success and check Cache and Network updated`() = runBlocking {

        val newSession = sessionFactory.createSingleSession(id = UUID.randomUUID().toString())
        collectFlowForSession(newSession){ value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                INSERT_SESSION_SUCCESS
            )
        }

        //check cache was inserted
        val cacheSessionInserted = sessionCacheDataSource.searchSessionById(newSession.id)
        assertTrue(cacheSessionInserted == newSession)

        //check network was inserted
        val networkSessionInserted = sessionNetworkDataSource.searchSessionById(newSession.id)
        assertTrue(networkSessionInserted == newSession)
    }

    @InternalCoroutinesApi
    @Test
    fun `Insert new session Fail and check Cache and Network unchanged`() = runBlocking {

        val newSession = sessionFactory.createSingleSession(id = FORCE_GENERAL_FAILURE)

        collectFlowForSession(newSession){ value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                INSERT_SESSION_FAILED
            )
        }

        //check cache was NOT inserted
        val cacheSessionInserted = sessionCacheDataSource.searchSessionById(newSession.id)
        assertNull(cacheSessionInserted)

        //check network was NOT inserted
        val networkSessionInserted = sessionNetworkDataSource.searchSessionById(newSession.id)
        assertNull(networkSessionInserted)
    }

    @InternalCoroutinesApi
    @Test
    fun `Insert new session Throws Exception and check Cache and Network unchanged`() = runBlocking {

        val newSession = sessionFactory.createSingleSession(id = FORCE_NEW_SESSION_EXCEPTION)

        collectFlowForSession(newSession){ value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN)?: false
            )
        }

        //check cache was NOT inserted
        val cacheSessionInserted = sessionCacheDataSource.searchSessionById(newSession.id)
        assertNull(cacheSessionInserted)

        //check network was NOT inserted
        val networkSessionInserted = sessionNetworkDataSource.searchSessionById(newSession.id)
        assertNull(networkSessionInserted)
    }

    @InternalCoroutinesApi
    private suspend fun collectFlowForSession(
        newSession: Session,
        emitResult: (value: DataState<SessionListViewState>?) -> Unit
    ) {
        insertNewSession.insertNewSession(
            id = newSession.id,
            stateEvent = SessionListStateEvent.InsertNewSessionEvent()
        ).collect(object: FlowCollector<DataState<SessionListViewState>?>{
            override suspend fun emit(value: DataState<SessionListViewState>?) {
                emitResult.invoke(value)
            }
        })
    }
}