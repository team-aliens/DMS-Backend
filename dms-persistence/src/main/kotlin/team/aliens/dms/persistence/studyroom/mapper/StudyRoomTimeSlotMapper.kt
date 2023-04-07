package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntity
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntityId
import team.aliens.dms.persistence.studyroom.repository.StudyRoomJpaRepository
import team.aliens.dms.persistence.studyroom.repository.TimeSlotJpaRepository

@Component
class StudyRoomTimeSlotMapper(
    private val studyRoomRepository: StudyRoomJpaRepository,
    private val timeSlotRepository: TimeSlotJpaRepository
) : GenericMapper<StudyRoomTimeSlot, StudyRoomTimeSlotJpaEntity> {

    override fun toDomain(entity: StudyRoomTimeSlotJpaEntity?): StudyRoomTimeSlot? {
        return entity?.let {
            StudyRoomTimeSlot(
                studyRoomId = it.studyRoom!!.id!!,
                timeSlotId = it.timeSlot!!.id!!
            )
        }
    }

    override fun toEntity(domain: StudyRoomTimeSlot): StudyRoomTimeSlotJpaEntity {
        val studyRoom = studyRoomRepository.findByIdOrNull(domain.studyRoomId)
        val timeSlot = timeSlotRepository.findByIdOrNull(domain.timeSlotId)

        return StudyRoomTimeSlotJpaEntity(
            id = StudyRoomTimeSlotJpaEntityId(
                studyRoomId = domain.studyRoomId,
                timeSlotId = domain.timeSlotId
            ),
            studyRoom = studyRoom,
            timeSlot = timeSlot
        )
    }
}
