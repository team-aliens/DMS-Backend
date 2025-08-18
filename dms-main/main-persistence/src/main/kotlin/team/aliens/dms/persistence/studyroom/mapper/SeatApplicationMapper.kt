package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntity
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntityId
import team.aliens.dms.persistence.studyroom.repository.SeatJpaRepository
import team.aliens.dms.persistence.studyroom.repository.TimeSlotJpaRepository

@Component
class SeatApplicationMapper(
    private val seatRepository: SeatJpaRepository,
    private val timeSlotRepository: TimeSlotJpaRepository,
    private val studentRepository: StudentJpaRepository
) : GenericMapper<SeatApplication, SeatApplicationJpaEntity> {

    override fun toDomain(entity: SeatApplicationJpaEntity?): SeatApplication? {
        return entity?.let {
            SeatApplication(
                seatId = it.seat!!.id!!,
                timeSlotId = it.timeSlot!!.id!!,
                studentId = it.student!!.id!!
            )
        }
    }

    override fun toEntity(domain: SeatApplication): SeatApplicationJpaEntity {
        val seat = seatRepository.findByIdOrNull(domain.seatId)
        val timeSlot = timeSlotRepository.findByIdOrNull(domain.timeSlotId)
        val student = studentRepository.findByIdOrNull(domain.studentId)

        return SeatApplicationJpaEntity(
            id = SeatApplicationJpaEntityId(
                seatId = domain.seatId,
                timeSlotId = domain.timeSlotId
            ),
            seat = seat,
            timeSlot = timeSlot,
            student = student
        )
    }
}
