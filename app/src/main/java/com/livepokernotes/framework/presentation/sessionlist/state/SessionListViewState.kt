package com.livepokernotes.framework.presentation.sessionlist.state

import android.os.Parcelable
import com.codingwithmitch.cleannotes.business.domain.state.ViewState
import com.livepokernotes.business.domain.model.Session
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SessionListViewState (
    var sessionList: ArrayList<Session>? = null,
    var newSession: Session? = null,
    var sessionPendingDelete: SessionPendingDelete? = null,
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null,
    var layoutManagerState: Parcelable? = null,
    var numSessionsInCache: Int? = null

) : Parcelable, ViewState {

    @Parcelize
    data class SessionPendingDelete(
        var session: Session? = null,
        var listPosition: Int? = null
    ) : Parcelable
}