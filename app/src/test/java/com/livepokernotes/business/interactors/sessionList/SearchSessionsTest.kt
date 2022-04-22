package com.livepokernotes.business.interactors.sessionList

import com.livepokernotes.business.data.SessionDataFactory
import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.business.domain.state.DataState
import com.livepokernotes.business.interactors.sessionlist.SearchSessions
import com.livepokernotes.business.interactors.sessionlist.SearchSessions.Companion.SEARCH_SESSIONS_SUCCESS
import com.livepokernotes.di.DependencyContainer
import com.livepokernotes.framework.datasource.database.ORDER_BY_ASC_COMMENT
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListStateEvent.*
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListViewState
import com.livepokernotes.util.isUnitTest
import junit.framework.Assert.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class SearchSessionsTest {

    //System in test
    private val searchSessions: SearchSessions

    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val sessionCacheDataSource: SessionCacheDataSource
    private val sessionFactory: SessionFactory

    init {
        isUnitTest = true
        dependencyContainer.build()
        sessionCacheDataSource = dependencyContainer.sessionCacheDataSource
        sessionFactory = dependencyContainer.sessionFactory
        searchSessions = SearchSessions(
            sessionCacheDataSource = sessionCacheDataSource
        )
    }

    @Test
    fun `Blank query Sessions are retrieved from cache`() = runBlocking {
        val query = ""
        var results: ArrayList<Session>? = null

        collectFlowForSearchSession(query, ORDER_BY_ASC_COMMENT, 1){ value ->
            assertEquals(value?.stateMessage?.response?.message, SEARCH_SESSIONS_SUCCESS)
            value?.data?.sessionList?.let { list ->
                results = ArrayList(list)
            }
        }

        assertNotNull(results)
        val sessionsInCache = sessionCacheDataSource.searchSessions(
            query = query,
            filterAndOrder = ORDER_BY_ASC_COMMENT,
            page = 1
        )
        assertTrue(results?.containsAll(sessionsInCache)?: false)
    }

    private suspend fun collectFlowForSearchSession(
        query: String,
        filterAndOrder: String,
        page: Int,
        emitResult: (value: DataState<SessionListViewState>?) -> Unit
    ) {
        searchSessions.searchSessions(
            query = query,
            filterAndOrder = filterAndOrder,
            page = page,
            stateEvent = SearchSessionsEvent()
        ).collect(object : FlowCollector<DataState<SessionListViewState>>{
            override suspend fun emit(value: DataState<SessionListViewState>) {
                emitResult.invoke(value)
            }
        } )
    }

}