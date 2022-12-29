package team.aliens.dms.persistence.studyroom

import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.spi.vo.SeatVO
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.StudyRoomPort
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatJpaEntity.seatJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatTypeJpaEntity.seatTypeJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QStudyRoomJpaEntity.studyRoomJpaEntity
import team.aliens.dms.persistence.studyroom.mapper.SeatMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomMapper
import team.aliens.dms.persistence.studyroom.repository.SeatJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomJpaRepository
import team.aliens.dms.persistence.studyroom.repository.vo.QQuerySeatVO
import team.aliens.dms.persistence.studyroom.repository.vo.QQueryStudyRoomVO

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

    override fun existsStudyRoomByFloorAndNameAndSchoolId(
        floor: Int, name: String, schoolId: UUID
    ) = studyRoomRepository.existsByNameAndFloorAndSchoolId(name, floor, schoolId)

    override fun queryAllSeatsByStudyRoomId(studyRoomId: UUID): List<SeatVO> {
        return jpaQueryFactory
            .select(
                QQuerySeatVO(
                    seatJpaEntity.id,
                    seatJpaEntity.widthLocation,
                    seatJpaEntity.heightLocation,
                    seatJpaEntity.number,
                    seatJpaEntity.status,
                    seatTypeJpaEntity.id,
                    seatTypeJpaEntity.name,
                    seatTypeJpaEntity.color,
                    studentJpaEntity.id,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    studentJpaEntity.profileImageUrl
                )
            )
            .from(seatJpaEntity)
            .leftJoin(seatJpaEntity.type, seatTypeJpaEntity)
            .leftJoin(seatJpaEntity.student, studentJpaEntity)
            .where(seatJpaEntity.studyRoom.id.eq(studyRoomId))
            .fetch()
    }

    override fun queryAllStudyRoomsBySchoolId(schoolId: UUID): List<StudyRoomVO> {
        return jpaQueryFactory
            .select(
                QQueryStudyRoomVO(
                    studyRoomJpaEntity.id,
                    studyRoomJpaEntity.floor,
                    studyRoomJpaEntity.name,
                    studyRoomJpaEntity.availableGrade,
                    studyRoomJpaEntity.availableSex,
                    studyRoomJpaEntity.inUseHeadcount,
                    studyRoomJpaEntity.availableHeadcount
                )
            )
            .from(studyRoomJpaEntity)
            .where(studyRoomJpaEntity.school.id.eq(schoolId))
            .orderBy(
                studyRoomJpaEntity.floor.asc(),
                studyRoomJpaEntity.name.asc()
            )
            .fetch()
    }

    override fun querySeatByStudyRoomId(studyRoomId: UUID) = seatMapper.toDomain(
        seatRepository.findByStudyRoomId(studyRoomId)
    )

    override fun saveSeat(seat: Seat) = seatMapper.toDomain(
        seatRepository.save(
            seatMapper.toEntity(seat)
        )
    )!!

    override fun saveAllSeat(seats: List<Seat>) {
        seatRepository.saveAll(
            seats.map { seatMapper.toEntity(it) }
        )
    }

    override fun deleteAllSeatsByStudyRoomId(studyRoomId: UUID) {
        seatRepository.deleteAllByStudyRoomId(studyRoomId)
        seatRepository.flush()
    }

    override fun saveStudyRoom(studyRoom: StudyRoom) = studyRoomMapper.toDomain(
        studyRoomRepository.save(
            studyRoomMapper.toEntity(studyRoom)
        )
    )!!

    override fun deleteStudyRoomById(studyRoomId: UUID) {
        studyRoomRepository.deleteById(studyRoomId)
    }

    override fun existsSeatBySeatTypeId(seatTypeId: UUID) = seatRepository.existsByTypeId(seatTypeId)
}