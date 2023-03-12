package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.StudyRoomInfo
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomInfoJpaEntity

@Component
class StudyRoomInfoMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<StudyRoomInfo, StudyRoomInfoJpaEntity> {

    override fun toDomain(entity: StudyRoomInfoJpaEntity?): StudyRoomInfo? {
        return entity?.let {
            StudyRoomInfo(
                id = it.id!!,
                schoolId = entity.school!!.id!!,
                name = entity.name,
                floor = entity.floor,
                widthSize = entity.widthSize,
                heightSize = entity.heightSize,
                availableHeadcount = entity.availableHeadcount,
                availableSex = entity.availableSex,
                availableGrade = entity.availableGrade,
                eastDescription = entity.eastDescription,
                westDescription = entity.westDescription,
                southDescription = entity.southDescription,
                northDescription = entity.northDescription
            )
        }
    }

    override fun toEntity(domain: StudyRoomInfo): StudyRoomInfoJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return StudyRoomInfoJpaEntity(
            id = domain.id,
            school = school,
            name = domain.name,
            floor = domain.floor,
            widthSize = domain.widthSize,
            heightSize = domain.heightSize,
            availableHeadcount = domain.availableHeadcount,
            availableSex = domain.availableSex,
            availableGrade = domain.availableGrade,
            eastDescription = domain.eastDescription,
            westDescription = domain.westDescription,
            southDescription = domain.southDescription,
            northDescription = domain.northDescription
        )
    }
}
