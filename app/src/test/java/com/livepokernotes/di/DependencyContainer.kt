package com.livepokernotes.di

import com.livepokernotes.business.data.cache.FakeSessionCacheDataSourceImpl
import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.network.FakeSessionNetworkDataSourceImpl
import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.business.domain.util.DateUtil
import com.livepokernotes.util.isUnitTest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DependencyContainer {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil = DateUtil(dateFormat)
    lateinit var sessionNetworkDataSource: SessionNetworkDataSource
    lateinit var sessionCacheDataSource: SessionCacheDataSource
    lateinit var sessionFactory: SessionFactory

    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {
        sessionFactory = SessionFactory(dateUtil)
        sessionNetworkDataSource = FakeSessionNetworkDataSourceImpl(
            sessionsData = HashMap(),
            deletedSessionsData = HashMap()
        )
        sessionCacheDataSource = FakeSessionCacheDataSourceImpl(
            sessionsData = HashMap(),
            dateUtil = dateUtil
        )
    }

}