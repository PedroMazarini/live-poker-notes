package com.livepokernotes.business.interactors.sessionlist

import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.business.domain.state.*
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListViewState
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
    ): Flow<DataState<SessionListViewState>> = flow {
        val newSession = sessionFactory.createSingleSession()
        val cacheResult = sessionCacheDataSource.insertSession(newSession)

        var cacheResponse: DataState<SessionListViewState>? = null
        if(cacheResult > 0){
            val viewState = SessionListViewState(
                newSession = newSession
            )
            cacheResponse = DataState.data(
                response = Response(
                    message = INSERT_SESSION_SUCCESS,
                    uiComponentType = UIComponentType.Toast(),
                    messageType = MessageType.Success()
                ),
                data = viewState,
                stateEvent = stateEvent
            )
        } else{
            cacheResponse = DataState.data(
                response = Response(
                    message = INSERT_SESSION_FAILED,
                    uiComponentType = UIComponentType.Toast(),
                    messageType = MessageType.Error()
                ),
                stateEvent = stateEvent
            )
        }
        emit(cacheResponse)

        updateNetwork(cacheResponse.stateMessage?.response?.message, newSession)
    }

    private suspend fun updateNetwork(cacheResponse: String?, newSession: Session) {
        if(cacheResponse.equals(INSERT_SESSION_SUCCESS)){
            sessionNetworkDataSource.insertSession(newSession)
        }
    }

    companion object{
        const val INSERT_SESSION_SUCCESS = "Successfully inserted new session"
        const val INSERT_SESSION_FAILED = "Failed to insert new session"
    }
}