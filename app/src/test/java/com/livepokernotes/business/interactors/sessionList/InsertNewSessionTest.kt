package com.livepokernotes.business.interactors.sessionList

import com.livepokernotes.business.data.cache.abstraction.SessionCacheDataSource
import com.livepokernotes.business.data.network.abstraction.SessionNetworkDataSource
import com.livepokernotes.business.domain.model.SessionFactory
import com.livepokernotes.business.interactors.sessionlist.InsertNewSession
import com.livepokernotes.di.DependencyContainer

class InsertNewSessionTest {

    //system in test
    private val insertNewSession: InsertNewSession

    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val sessionCacheDataSource: SessionCacheDataSource
    private val sessionNetworkDataSource: SessionNetworkDataSource
    private val sessionFactory: SessionFactory

    init {
        dependencyContainer.build()
        sessionCacheDataSource = dependencyContainer.sessionCacheDataSource
        sessionNetworkDataSource = dependencyContainer.sessionNetworkDataSource
        sessionFactory = dependencyContainer.sessionFactory
        insertNewSession = InsertNewSession(
            sessionCacheDataSource = sessionCacheDataSource,
            sessionNetworkDataSource = sessionNetworkDataSource,
            sessionFactory = sessionFactory
        )
    }
}