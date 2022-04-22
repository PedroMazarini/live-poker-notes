package com.livepokernotes.framework.presentation.sessionlist.state

import com.livepokernotes.business.domain.state.StateEvent
import com.livepokernotes.framework.datasource.cache.mappers.CacheMapper

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

    class SearchSessionsEvent(
        val clearLayoutManagerState: Boolean = true
    ): SessionListStateEvent() {

        override fun errorInfo(): String {
            return "Error searching sessions."
        }

        override fun eventName(): String {
            return "SearchSessionsEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetNumSessionsInCacheEvent : SessionListStateEvent() {

        override fun errorInfo(): String {
            return "Error getting number of sessions."
        }

        override fun eventName(): String {
            return "GetNumSessionsEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }
}