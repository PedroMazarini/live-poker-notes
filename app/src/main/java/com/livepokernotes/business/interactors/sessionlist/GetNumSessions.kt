package com.livepokernotes.business.interactors.sessionlist

import com.livepokernotes.business.data.cache.CacheResponseHandler
import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.util.safeCacheCall
import com.livepokernotes.business.domain.state.*
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumSessions(
    private val sessionCacheDataSource: SessionCacheDataSource
) {

    fun getNumSessions(
        stateEvent: StateEvent
    ): Flow<DataState<SessionListViewState>> = flow {

        val cacheResult = safeCacheCall(IO) {
            sessionCacheDataSource.getNumSessions()
        }

        val response = object : CacheResponseHandler<SessionListViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override fun handleSuccess(resultObj: Int): DataState<SessionListViewState> {
                val viewState = SessionListViewState(
                    numSessionsInCache = resultObj
                )
                return DataState.data(
                    response = Response(
                        message = GET_NUM_SESSIONS_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()
        emit(response)
    }

    companion object {
        const val GET_NUM_SESSIONS_SUCCESS =
            "Successfully retrieved the number of sessions in cache"
    }

}