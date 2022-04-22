package com.livepokernotes.business.interactors.sessionlist

import com.livepokernotes.business.data.cache.CacheResponseHandler
import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.util.safeCacheCall
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.state.*
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchSessions(
    private val sessionCacheDataSource: SessionCacheDataSource
) {

    fun searchSessions(
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<SessionListViewState>> = flow {
        var verifiedPage = if (page > 0) page else 1

        val cacheResult = safeCacheCall(IO) {
            sessionCacheDataSource.searchSessions(
                query = query,
                filterAndOrder = filterAndOrder,
                page = verifiedPage
            )
        }

        val response = object : CacheResponseHandler<SessionListViewState, List<Session>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override fun handleSuccess(resultObj: List<Session>): DataState<SessionListViewState> {
                var message: String? = SEARCH_SESSIONS_SUCCESS
                var uiComponentType: UIComponentType = UIComponentType.None()
                if (resultObj.isEmpty()) {
                    message = SEARCH_SESSIONS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType = MessageType.Success()
                    ),
                    data = SessionListViewState(
                        sessionList = ArrayList(resultObj)
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()
        emit(response)
    }

    companion object {
        val SEARCH_SESSIONS_SUCCESS = "Successfully retrieved list of sessions."
        val SEARCH_SESSIONS_NO_MATCHING_RESULTS = "There are no sessions that match that query."
        val SEARCH_SESSIONS_FAILED = "Failed to retrieve the list of sessions."

    }

}