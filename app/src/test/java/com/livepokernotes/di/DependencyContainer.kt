package com.livepokernotes.di

import com.livepokernotes.business.data.SessionDataFactory
import com.livepokernotes.business.data.cache.FakeSessionCacheDataSourceImpl
import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.network.FakeSessionNetworkDataSourceImpl
import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.util.DateUtil
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
    lateinit var sessionDataFactory: SessionDataFactory
    private var sessionsData: HashMap<String, Session> = HashMap()

    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {
        initializeFakeDataSet()
        sessionFactory = SessionFactory(dateUtil)
        sessionNetworkDataSource = FakeSessionNetworkDataSourceImpl(
            sessionsData = sessionsData,
            deletedSessionsData = HashMap()
        )
        sessionCacheDataSource = FakeSessionCacheDataSourceImpl(
            sessionsData = sessionsData,
            dateUtil = dateUtil
        )
    }

    private fun initializeFakeDataSet() {
        this.javaClass.classLoader?.let { classLoader ->
            sessionDataFactory = SessionDataFactory(classLoader)
            sessionsData = sessionDataFactory.produceHashMapOfSessions(
                sessionDataFactory.produceListOfSessions()
            )
        }
    }

}