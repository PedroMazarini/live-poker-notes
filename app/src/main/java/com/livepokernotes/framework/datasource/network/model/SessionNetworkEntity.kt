package com.livepokernotes.framework.datasource.network.model

import com.google.firebase.Timestamp

data class SessionNetworkEntity(
    var id: String,
    var comment: String,
    var updated_at: Timestamp,
    var created_at: Timestamp
){
    constructor(): this(
        "",
        "",
        Timestamp.now(),
        Timestamp.now()
    )
}