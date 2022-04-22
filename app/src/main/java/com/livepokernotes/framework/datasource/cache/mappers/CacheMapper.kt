package com.livepokernotes.framework.datasource.cache.mappers

import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.util.EntityMapper
import com.livepokernotes.framework.datasource.cache.model.SessionCacheEntity

object CacheMapper : EntityMapper<SessionCacheEntity, Session>() {

    override fun SessionCacheEntity.mapToDomainModel(): Session =
        Session(
            id = this.id,
            comment = this.comment,
            updated_at = this.updated_at,
            created_at = this.created_at
        )

    override fun Session.mapToEntity(): SessionCacheEntity =
        SessionCacheEntity(
            id = this.id,
            comment = this.comment,
            updated_at = this.updated_at,
            created_at = this.created_at
        )

    override fun List<SessionCacheEntity>.mapToDomainList(): List<Session>
        = this.map { it.mapToDomainModel() }

    override fun List<Session>.mapToEntityList(): List<SessionCacheEntity>
        = this.map { it.mapToEntity() }

}