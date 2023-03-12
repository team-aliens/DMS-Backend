package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.studyroom.entity.StudyRoomJpaEntity
import team.aliens.dms.persistence.studyroom.repository.StudyRoomInfoJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomTimeSlotJpaRepository

@Component
class StudyRoomMapper(
    private val studyRoomRepository: StudyRoomInfoJpaRepository,
    private val timeSlotRepository: StudyRoomTimeSlotJpaRepository
) : GenericMapper<StudyRoom, StudyRoomJpaEntity> {

    override fun toDomain(entity: StudyRoomJpaEntity?): StudyRoom? {
        return entity?.let {
            StudyRoom(
                id = it.id!!,
                inUseHeadcount = it.inUseHeadcount,
                studyRoomInfoId = it.studyRoomInfo!!.id!!,
                timeSlotId = it.timeSlot?.id
            )
        }
    }

    override fun toEntity(domain: StudyRoom): StudyRoomJpaEntity {
        val studyRoom = studyRoomRepository.findByIdOrNull(domain.studyRoomInfoId)
        val timeSlot = timeSlotRepository.findByIdOrNull(domain.studyRoomInfoId)

        return StudyRoomJpaEntity(
            id = domain.id,
            inUseHeadcount = domain.inUseHeadcount,
            studyRoomInfo = studyRoom,
            timeSlot = timeSlot
        )
    }
}
