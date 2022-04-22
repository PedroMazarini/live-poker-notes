package com.livepokernotes.framework.datasource.network.mappers

import com.livepokernotes.business.domain.model.Session
import com.livepokernotes.business.domain.util.EntityMapper
import com.livepokernotes.framework.datasource.network.model.SessionNetworkEntity
import com.livepokernotes.util.DateUtil.toFormattedStringData
import com.livepokernotes.util.DateUtil.toTimestamp

object NetworkMapper: EntityMapper<SessionNetworkEntity, Session>() {

    override fun SessionNetworkEntity.mapToDomainModel(): Session =
        Session(
            id = this.id,
            comment = this.comment,
            updated_at = this.updated_at.toFormattedStringData(),
            created_at = this.created_at.toFormattedStringData()
        )

    override fun Session.mapToEntity(): SessionNetworkEntity =
        SessionNetworkEntity(
            id = this.id,
            comment = this.comment,
            updated_at = this.updated_at.toTimestamp(),
            created_at = this.created_at.toTimestamp()
        )

    override fun List<SessionNetworkEntity>.mapToDomainList(): List<Session>
            = this.map { it.mapToDomainModel() }

    override fun List<Session>.mapToEntityList(): List<SessionNetworkEntity>
            = this.map { it.mapToEntity() }

}