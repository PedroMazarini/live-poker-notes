package com.livepokernotes.business.domain.model

import com.livepokernotes.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionFactory
@Inject
constructor(
    private val dateUtil: DateUtil
){
    fun createSingleSession(
        id: String? = null,
        comment: String? = null,
        created_at: String? = null
    ): Session =
        Session(
            id = id ?: UUID.randomUUID().toString(),
            comment = comment ?: "",
            created_at = created_at?: dateUtil.getCurrentDate(),
            updated_at = dateUtil.getCurrentDate()
        )

}