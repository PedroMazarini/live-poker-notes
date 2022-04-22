package com.livepokernotes.business.interactors.common

import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.data.util.safeCacheCall
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.state.DataState
import com.livepokernotes.business.domain.state.StateEvent
import com.livepokernotes.framework.presentation.sessionlist.state.SessionListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteSession(
    private val sessionCacheDataSource: SessionCacheDataSource,
    private val sessionNetworkDataSource: SessionNetworkDataSource
) {

    fun deleteSession(
        session: Session,
        stateEvent: StateEvent
    ): Flow<DataState<SessionListViewState>> = flow {

        val cacheResult = safeCacheCall(IO) {
            sessionCacheDataSource.deleteSession(session.id)
        }
    }
}
