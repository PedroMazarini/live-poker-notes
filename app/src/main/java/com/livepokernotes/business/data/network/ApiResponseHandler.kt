package com.livepokernotes.business.data.network

import com.livepokernotes.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.livepokernotes.business.data.network.NetworkErrors.NETWORK_ERROR
import com.livepokernotes.business.domain.state.*

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
){
    suspend fun getResult(): DataState<ViewState>{
        return when(response) {
            is ApiResult.GenericError -> getErrorResponse(response.errorMessage)
            is ApiResult.NetworkError -> getErrorResponse(NETWORK_ERROR)
            is ApiResult.Success -> {
                if(response.value == null) getErrorResponse(NETWORK_DATA_NULL)
                else handleSuccess(response.value)
            }
        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

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