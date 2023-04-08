package team.aliens.dms.persistence.studyroom

import com.querydsl.core.types.Expression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.StudyRoomPort
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatApplicationJpaEntity.seatApplicationJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatJpaEntity.seatJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatTypeJpaEntity.seatTypeJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QStudyRoomJpaEntity.studyRoomJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QStudyRoomTimeSlotJpaEntity.studyRoomTimeSlotJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QTimeSlotJpaEntity.timeSlotJpaEntity
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntityId
import team.aliens.dms.persistence.studyroom.mapper.SeatApplicationMapper
import team.aliens.dms.persistence.studyroom.mapper.SeatMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomTimeSlotMapper
import team.aliens.dms.persistence.studyroom.mapper.TimeSlotMapper
import team.aliens.dms.persistence.studyroom.repository.SeatApplicationJpaRepository
import team.aliens.dms.persistence.studyroom.repository.SeatJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomTimeSlotJpaRepository
import team.aliens.dms.persistence.studyroom.repository.TimeSlotJpaRepository
import team.aliens.dms.persistence.studyroom.repository.vo.QQuerySeatApplicationVO
import team.aliens.dms.persistence.studyroom.repository.vo.QQueryStudyRoomVO
import team.aliens.dms.persistence.studyroom.repository.vo.QStudentSeatApplicationVO
import team.aliens.dms.persistence.studyroom.repository.vo.QueryStudyRoomVO
import java.time.LocalTime
import java.util.UUID

@Component
class StudyRoomPersistenceAdapter(
    private val studyRoomMapper: StudyRoomMapper,
    private val seatMapper: SeatMapper,
    private val timeSlotMapper: TimeSlotMapper,
    private val studyRoomTimeSlotMapper: StudyRoomTimeSlotMapper,
    private val seatApplicationMapper: SeatApplicationMapper,
    private val studyRoomRepository: StudyRoomJpaRepository,
    private val seatRepository: SeatJpaRepository,
    private val timeSlotRepository: TimeSlotJpaRepository,
    private val studyRoomTimeSlotRepository: StudyRoomTimeSlotJpaRepository,
    private val seatApplicationRepository: SeatApplicationJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : StudyRoomPort {

    override fun queryStudyRoomById(studyRoomId: UUID) = studyRoomMapper.toDomain(
        studyRoomRepository.findByIdOrNull(studyRoomId)
    )

    override fun querySeatById(seatId: UUID) = seatMapper.toDomain(
        seatRepository.findByIdOrNull(seatId)
    )

    override fun querySeatApplicationsByStudentId(studentId: UUID) =
        seatApplicationRepository.queryByStudentId(studentId)
            .map { seatApplicationMapper.toDomain(it)!! }

    override fun queryAllSeatsById(seatIds: List<UUID>) =
        seatRepository.findAllById(seatIds)
            .map { seatMapper.toDomain(it)!! }

    override fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID) =
        studyRoomRepository.existsByNameAndFloorAndSchoolId(name, floor, schoolId)

    override fun queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(
        studyRoomId: UUID,
        timeSlotId: UUID,
    ): List<SeatApplicationVO> {
        return jpaQueryFactory
            .select(
                QQuerySeatApplicationVO(
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
            .join(seatJpaEntity.studyRoom, studyRoomJpaEntity)
            .leftJoin(seatJpaEntity.type, seatTypeJpaEntity)
            .leftJoin(seatApplicationJpaEntity)
            .on(
                seatJpaEntity.id.eq(seatApplicationJpaEntity.seat.id),
                seatApplicationJpaEntity.timeSlot.id.eq(timeSlotId)
            )
            .leftJoin(seatApplicationJpaEntity.student, studentJpaEntity)
            .where(
                seatJpaEntity.studyRoom.id.eq(studyRoomId)
            )
            .fetch()
    }

    override fun queryAllStudyRoomsByTimeSlotId(timeSlotId: UUID): List<StudyRoomVO> {
        return selectStudyRoomVOByTimeSlotId(timeSlotId)
            .orderBy(
                studyRoomJpaEntity.floor.asc(),
                studyRoomJpaEntity.name.asc()
            )
            .fetch()
    }

    override fun queryAllStudyRoomsByTimeSlotIdAndGradeAndSex(timeSlotId: UUID, grade: Int, sex: Sex): List<StudyRoomVO> {
        return selectStudyRoomVOByTimeSlotId(timeSlotId)
            .where(
                studyRoomJpaEntity.availableGrade.`in`(grade, 0),
                studyRoomJpaEntity.availableSex.`in`(sex, Sex.ALL)
            )
            .orderBy(
                studyRoomJpaEntity.floor.asc(),
                studyRoomJpaEntity.name.asc()
            )
            .fetch()
    }

    private fun selectStudyRoomVOByTimeSlotId(timeSlotId: UUID): JPAQuery<QueryStudyRoomVO> =
        jpaQueryFactory
            .select(
                QQueryStudyRoomVO(
                    studyRoomJpaEntity.id,
                    studyRoomJpaEntity.floor,
                    studyRoomJpaEntity.name,
                    studyRoomJpaEntity.availableGrade,
                    studyRoomJpaEntity.availableSex,
                    seatApplicationJpaEntity.count().intValue() as Expression<Int>,
                    studyRoomJpaEntity.availableHeadcount
                )
            )
            .from(studyRoomJpaEntity)
            .join(studyRoomTimeSlotJpaEntity)
            .on(
                studyRoomTimeSlotJpaEntity.studyRoom.id.eq(studyRoomJpaEntity.id),
                studyRoomTimeSlotJpaEntity.timeSlot.id.eq(timeSlotId)
            )
            .leftJoin(seatJpaEntity)
            .on(studyRoomJpaEntity.id.eq(seatJpaEntity.studyRoom.id))
            .leftJoin(seatApplicationJpaEntity)
            .on(
                seatJpaEntity.id.eq(seatApplicationJpaEntity.seat.id),
                seatApplicationJpaEntity.timeSlot.id.eq(timeSlotId)
            )
            .groupBy(studyRoomJpaEntity.id)

    override fun queryTimeSlotsBySchoolId(schoolId: UUID) =
        timeSlotRepository.findBySchoolId(schoolId)
            .map { timeSlotMapper.toDomain(it)!! }

    override fun queryTimeSlotById(timeSlotId: UUID) = timeSlotMapper.toDomain(
        timeSlotRepository.findByIdOrNull(timeSlotId)
    )

    override fun queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId: UUID, studyRoomId: UUID): List<TimeSlot> {
        return jpaQueryFactory.selectFrom(timeSlotJpaEntity)
            .join(studyRoomTimeSlotJpaEntity)
            .on(
                studyRoomTimeSlotJpaEntity.timeSlot.id.eq(timeSlotJpaEntity.id),
                studyRoomTimeSlotJpaEntity.studyRoom.id.eq(studyRoomId)
            )
            .join(studyRoomTimeSlotJpaEntity.studyRoom, studyRoomJpaEntity)
            .where(
                studyRoomJpaEntity.school.id.eq(schoolId)
            )
            .fetch()
            .map {
                timeSlotMapper.toDomain(it)!!
            }
    }

    override fun querySeatApplicationsByStudentIdIn(studentIds: List<UUID>): List<StudentSeatApplicationVO> {
        return jpaQueryFactory
            .select(
                QStudentSeatApplicationVO(
                    seatApplicationJpaEntity.student.id,
                    studyRoomJpaEntity.name,
                    studyRoomJpaEntity.floor,
                    seatJpaEntity.number,
                    seatTypeJpaEntity.name,
                    seatApplicationJpaEntity.timeSlot.id
                )
            )
            .from(seatApplicationJpaEntity)
            .join(seatApplicationJpaEntity.seat, seatJpaEntity)
            .join(seatJpaEntity.studyRoom, studyRoomJpaEntity)
            .join(seatJpaEntity.type, seatTypeJpaEntity)
            .where(
                seatApplicationJpaEntity.student.id.`in`(studentIds)
            )
            .fetch()
    }

    override fun existsSeatApplicationBySeatIdAndTimeSlotId(seatId: UUID, timeSlotId: UUID) =
        jpaQueryFactory.selectFrom(seatApplicationJpaEntity)
            .where(
                seatApplicationJpaEntity.seat.id.eq(seatId),
                seatApplicationJpaEntity.timeSlot.id.eq(timeSlotId)
            )
            .fetchFirst() != null

    override fun existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId: UUID, timeSlotId: UUID) =
        studyRoomTimeSlotRepository.existsById(
            StudyRoomTimeSlotJpaEntityId(
                studyRoomId = studyRoomId,
                timeSlotId = timeSlotId
            )
        )

    override fun existsTimeSlotByStartTimeAndEndTime(startTime: LocalTime, endTime: LocalTime) =
        timeSlotRepository.existsByStartTimeAndEndTime(startTime, endTime)

    override fun existsStudyRoomTimeSlotByTimeSlotId(timeSlotId: UUID) =
        studyRoomTimeSlotRepository.existsByTimeSlotId(timeSlotId)

    override fun saveAllSeats(seats: List<Seat>) =
        seatRepository.saveAll(
            seats.map { seatMapper.toEntity(it) }
        ).map { seatMapper.toDomain(it)!! }

    override fun saveTimeSlot(timeSlot: TimeSlot) = timeSlotMapper.toDomain(
        timeSlotRepository.save(
            timeSlotMapper.toEntity(timeSlot)
        )
    )!!

    override fun saveSeatApplication(seatApplication: SeatApplication) = seatApplicationMapper.toDomain(
        seatApplicationRepository.save(
            seatApplicationMapper.toEntity(seatApplication)
        )
    )!!

    override fun saveStudyRoom(studyRoom: StudyRoom) = studyRoomMapper.toDomain(
        studyRoomRepository.save(
            studyRoomMapper.toEntity(studyRoom)
        )
    )!!

    override fun saveAllStudyRoomTimeSlots(studyRoomTimeSlots: List<StudyRoomTimeSlot>) =
        studyRoomTimeSlotRepository.saveAll(
            studyRoomTimeSlots.map { studyRoomTimeSlotMapper.toEntity(it) }
        ).map { studyRoomTimeSlotMapper.toDomain(it)!! }

    override fun deleteStudyRoomById(studyRoomId: UUID) {
        studyRoomRepository.deleteById(studyRoomId)
    }

    override fun deleteTimeSlotById(timeSlotId: UUID) {
        timeSlotRepository.deleteById(timeSlotId)
    }

    override fun deleteSeatApplicationByStudentId(studentId: UUID) {
        seatApplicationRepository.deleteByStudentId(studentId)
    }

    override fun deleteSeatApplicationByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID) {
        seatApplicationRepository.deleteByStudentIdAndTimeSlotId(studentId, timeSlotId)
    }

    override fun deleteSeatApplicationByTimeSlotId(timeSlotId: UUID) {
        seatApplicationRepository.deleteByTimeSlotId(timeSlotId)
    }

    override fun deleteSeatApplicationByStudyRoomId(studyRoomId: UUID) {
        seatApplicationRepository.deleteBySeatStudyRoomId(studyRoomId)
    }

    override fun deleteSeatByStudyRoomId(studyRoomId: UUID) {
        seatRepository.deleteByStudyRoomId(studyRoomId)
        seatRepository.flush()
    }

    override fun deleteAllSeatApplications() {
        seatApplicationRepository.deleteAll()
    }

    override fun deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId: UUID) {
        studyRoomTimeSlotRepository.deleteByStudyRoomId(studyRoomId)
    }

    override fun existsSeatBySeatTypeId(seatTypeId: UUID) = seatRepository.existsByTypeId(seatTypeId)
}
