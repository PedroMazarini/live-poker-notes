package com.livepokernotes.business.data.cache

import com.livepokernotes.business.data.cache.CacheErrors.CACHE_DATA_NULL
import com.livepokernotes.business.domain.state.*

abstract class CacheResponseHandler<ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
){

    suspend fun getResult(): DataState<ViewState>?{
        return when(response){
            is CacheResult.GenericError -> getErrorResponse(response.errorMessage)
            is CacheResult.Success -> {
                if(response.value == null) getErrorResponse(CACHE_DATA_NULL)
                else handleSuccess(resultObj = response.value)
            }
        }
    }

    abstract fun handleSuccess(resultObj: Data): DataState<ViewState>

    private fun getErrorResponse(reason: String?) =
        DataState.error<ViewState>(
            response = Response(
                message = "${stateEvent?.errorInfo()}"+
                        "Reason: $reason",
                uiComponentType = UIComponentType.Dialog(),
                messageType = MessageType.Error()
            ),
            stateEvent = stateEvent
        )
}