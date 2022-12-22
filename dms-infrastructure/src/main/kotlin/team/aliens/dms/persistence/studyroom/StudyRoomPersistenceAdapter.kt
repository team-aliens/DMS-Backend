package team.aliens.dms.persistence.studyroom

import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement.StudentElement
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement.TypeElement
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.StudyRoomPort
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatJpaEntity.seatJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatTypeJpaEntity.seatTypeJpaEntity
import team.aliens.dms.persistence.studyroom.mapper.SeatMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomMapper
import team.aliens.dms.persistence.studyroom.repository.SeatJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomJpaRepository

@Component
class StudyRoomPersistenceAdapter(
    private val studyRoomMapper: StudyRoomMapper,
    private val seatMapper: SeatMapper,
    private val studyRoomRepository: StudyRoomJpaRepository,
    private val seatRepository: SeatJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : StudyRoomPort {

    override fun queryStudyRoomById(studyRoomId: UUID) = studyRoomMapper.toDomain(
        studyRoomRepository.findByIdOrNull(studyRoomId)
    )

    override fun querySeatById(seatId: UUID) = seatMapper.toDomain(
        seatRepository.findByIdOrNull(seatId)
    )

    override fun querySeatByStudentId(studentId: UUID) = seatMapper.toDomain(
        seatRepository.findByStudentId(studentId)
    )

    override fun queryAllSeatByStudyRoomId(studyRoomId: UUID) = jpaQueryFactory
        .selectFrom(seatJpaEntity)
        .leftJoin(seatJpaEntity.student, studentJpaEntity).fetchJoin()
        .leftJoin(seatJpaEntity.type, seatTypeJpaEntity).fetchJoin()
        .where(seatJpaEntity.studyRoom.id.eq(studyRoomId))
        .fetch()
        .map {
            SeatElement(
                id = it.id!!,
                widthSize = it.widthLocation,
                heightSize = it.heightLocation,
                number = it.number,
                type = it.type?.run {
                    TypeElement(
                        id = this.id!!,
                        name = this.name,
                        color = this.color
                    )
                },
                status = it.status,
                isMine = null,
                student = it.student?.run {
                    StudentElement(
                        id = this.id,
                        name = this.name
                    )
                }
            )
        }

    override fun countSeatByStudyRoomIdAndStatus(studyRoomId: UUID, status: SeatStatus) =
        seatRepository.countByStudyRoomIdAndStatus(studyRoomId, status)

    override fun saveSeat(seat: Seat) = seatMapper.toDomain(
        seatRepository.save(
            seatMapper.toEntity(seat)
        )
    )!!
}