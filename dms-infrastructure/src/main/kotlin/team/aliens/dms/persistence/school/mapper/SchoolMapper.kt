package team.aliens.dms.persistence.school.mapper

import org.mapstruct.Mapper
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.entity.SchoolEntity

@Mapper
interface SchoolMapper : GenericMapper<School, SchoolEntity> {

    override fun toDomain(e: SchoolEntity): School

    override fun toEntity(d: School): SchoolEntity
}