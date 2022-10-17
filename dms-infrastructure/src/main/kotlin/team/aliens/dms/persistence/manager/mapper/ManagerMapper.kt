package team.aliens.dms.persistence.manager.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.manager.entity.ManagerEntity

@Mapper
interface ManagerMapper : GenericMapper<Manager, ManagerEntity> {

    @Mapping(source = "userEntity.id", target = "managerId")
    override fun toDomain(e: ManagerEntity): Manager

    @Mapping(source = "managerId", target = "userEntity.id")
    override fun toEntity(d: Manager): ManagerEntity
}