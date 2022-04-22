package com.livepokernotes.util

import com.google.firebase.Timestamp
import com.livepokernotes.framework.datasource.network.model.SessionNetworkEntity

object UtilExtensionFunctions {

    fun SessionNetworkEntity.updatedNow() = this.copy(updated_at = Timestamp.now())

}