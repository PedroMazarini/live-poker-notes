package com.livepokernotes.business.interactors.sessionlist

import com.livepokernotes.business.data.cache.CacheResponseHandler
import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.data.util.safeApiCall
import com.livepokernotes.business.data.util.safeCacheCall
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.business.domain.state.*
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertNewSession(
    private val sessionCacheDataSource: SessionCacheDataSource,
    private val sessionNetworkDataSource: SessionNetworkDataSource,
    private val sessionFactory: SessionFactory
) {

    fun insertNewSession(
        id: String? = null,
        stateEvent: StateEvent
    ): Flow<DataState<SessionListViewState>?> = flow {
        val newSession = sessionFactory.createSingleSession(id = id)
        val cacheResult = safeCacheCall(IO) {
            sessionCacheDataSource.insertSession(newSession)
        }

        val cacheResponse = object: CacheResponseHandler<SessionListViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override fun handleSuccess(resultObj: Long): DataState<SessionListViewState> {
                return if(resultObj > 0){
                    val viewState = SessionListViewState(
                        newSession = newSession
                    )
                    DataState.data(
                        response = Response(
                            message = INSERT_SESSION_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                } else{
                    DataState.data(
                        response = Response(
                            message = INSERT_SESSION_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(cacheResponse)

        updateNetwork(cacheResponse.stateMessage?.response?.message, newSession)
    }

    private suspend fun updateNetwork(cacheResponse: String?, newSession: Session) {
        if(cacheResponse.equals(INSERT_SESSION_SUCCESS)){
            safeApiCall(IO){
                sessionNetworkDataSource.insertSession(newSession)
            }
        }
    }

    companion object{
        const val INSERT_SESSION_SUCCESS = "Successfully inserted new session"
        const val INSERT_SESSION_FAILED = "Failed to insert new session"
    }
}