package com.livepokernotes.business.domain.util

abstract class EntityMapper<Entity, DomainModel> {

    abstract fun Entity.mapToDomainModel(): DomainModel

    abstract fun DomainModel.mapToEntity(): Entity

    abstract fun List<Entity>.mapToDomainList(): List<DomainModel>

    abstract fun List<DomainModel>.mapToEntityList(): List<Entity>

}