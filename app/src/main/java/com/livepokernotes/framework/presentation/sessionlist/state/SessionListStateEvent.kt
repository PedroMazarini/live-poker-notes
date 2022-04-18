package com.livepokernotes.framework.presentation.sessionlist.state

import com.livepokernotes.business.domain.state.StateEvent

sealed class SessionListStateEvent: StateEvent {

    class InsertNewSessionEvent(): SessionListStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting new session."
        }

        override fun eventName(): String {
            return "InsertNewSessionEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

}