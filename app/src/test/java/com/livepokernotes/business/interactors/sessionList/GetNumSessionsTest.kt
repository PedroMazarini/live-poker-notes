package com.livepokernotes.business.interactors.sessionList

import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.business.domain.state.DataState
import com.livepokernotes.business.interactors.sessionlist.GetNumSessions
import com.livepokernotes.business.interactors.sessionlist.GetNumSessions.Companion.GET_NUM_SESSIONS_SUCCESS
import com.livepokernotes.di.DependencyContainer
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListStateEvent
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListViewState
import com.livepokernotes.util.isUnitTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class GetNumSessionsTest {

    //system in test
    private val getNumSessions: GetNumSessions

    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val sessionCacheDataSource: SessionCacheDataSource
    private val sessionFactory: SessionFactory

    init {
        isUnitTest = true
        dependencyContainer.build()
        sessionCacheDataSource = dependencyContainer.sessionCacheDataSource

        sessionFactory = dependencyContainer.sessionFactory
        getNumSessions = GetNumSessions(
            sessionCacheDataSource = sessionCacheDataSource
        )
    }

    @Test
    fun `Get number of sessions Success confirm its correct`() = runBlocking {
        var numSessions = 0
        collectFlowForGetNumSessions { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_NUM_SESSIONS_SUCCESS
            )
            numSessions = value?.data?.numSessionsInCache ?: 0
        }

        val actualNumSessionsInCache = sessionCacheDataSource.getNumSessions()
        assertEquals(actualNumSessionsInCache, numSessions)
    }

    private suspend fun collectFlowForGetNumSessions(
        emitResult: (value: DataState<SessionListViewState>?) -> Unit
    ) {
        getNumSessions.getNumSessions(
            stateEvent = SessionListStateEvent.GetNumSessionsInCacheEvent
        ).collect(object : FlowCollector<DataState<SessionListViewState>> {
            override suspend fun emit(value: DataState<SessionListViewState>) {
                emitResult.invoke(value)
            }
        })
    }
}