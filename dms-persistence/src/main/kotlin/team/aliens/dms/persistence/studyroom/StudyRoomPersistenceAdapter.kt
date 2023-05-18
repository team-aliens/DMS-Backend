package team.aliens.dms.persistence.studyroom

import com.querydsl.core.types.Expression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatType
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
import team.aliens.dms.persistence.studyroom.mapper.AvailableTimeMapper
import team.aliens.dms.persistence.studyroom.mapper.SeatApplicationMapper
import team.aliens.dms.persistence.studyroom.mapper.SeatMapper
import team.aliens.dms.persistence.studyroom.mapper.SeatTypeMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomTimeSlotMapper
import team.aliens.dms.persistence.studyroom.mapper.TimeSlotMapper
import team.aliens.dms.persistence.studyroom.repository.AvailableTimeJpaRepository
import team.aliens.dms.persistence.studyroom.repository.SeatApplicationJpaRepository
import team.aliens.dms.persistence.studyroom.repository.SeatJpaRepository
import team.aliens.dms.persistence.studyroom.repository.SeatTypeJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomTimeSlotJpaRepository
import team.aliens.dms.persistence.studyroom.repository.TimeSlotJpaRepository
import team.aliens.dms.persistence.studyroom.repository.vo.QQuerySeatApplicationVO
import team.aliens.dms.persistence.studyroom.repository.vo.QQueryStudyRoomVO
import team.aliens.dms.persistence.studyroom.repository.vo.QStudentSeatApplicationVO
import java.time.LocalTime
import java.util.UUID

@Component
class StudyRoomPersistenceAdapter(
    private val studyRoomMapper: StudyRoomMapper,
    private val seatMapper: SeatMapper,
    private val seatTypeMapper: SeatTypeMapper,
    private val timeSlotMapper: TimeSlotMapper,
    private val studyRoomTimeSlotMapper: StudyRoomTimeSlotMapper,
    private val seatApplicationMapper: SeatApplicationMapper,
    private val availableTimeMapper: AvailableTimeMapper,
    private val studyRoomRepository: StudyRoomJpaRepository,
    private val seatRepository: SeatJpaRepository,
    private val seatTypeRepository: SeatTypeJpaRepository,
    private val timeSlotRepository: TimeSlotJpaRepository,
    private val studyRoomTimeSlotRepository: StudyRoomTimeSlotJpaRepository,
    private val seatApplicationRepository: SeatApplicationJpaRepository,
    private val availableTimeRepository: AvailableTimeJpaRepository,
    private val queryFactory: JPAQueryFactory
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

    override fun querySeatApplicationsByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID) = seatApplicationMapper.toDomain(
        seatApplicationRepository.queryByStudentIdAndTimeSlotId(studentId, timeSlotId)
    )

    override fun queryAllSeatsById(seatIds: List<UUID>) =
        seatRepository.findAllById(seatIds)
            .map { seatMapper.toDomain(it)!! }

    override fun queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(
        studyRoomId: UUID,
        timeSlotId: UUID,
    ): List<SeatApplicationVO> {
        return queryFactory
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

    override fun queryAllStudyRoomsByTimeSlotIdAndGradeAndSex(timeSlotId: UUID, grade: Int?, sex: Sex?): List<StudyRoomVO> {
        return queryFactory
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
            .where(
                grade?.let { studyRoomJpaEntity.availableGrade.`in`(grade, 0) },
                sex?.let { studyRoomJpaEntity.availableSex.`in`(sex, Sex.ALL) }
            )
            .orderBy(
                studyRoomJpaEntity.floor.asc(),
                studyRoomJpaEntity.name.asc()
            )
            .fetch()
    }

    override fun queryTimeSlotById(timeSlotId: UUID) = timeSlotMapper.toDomain(
        timeSlotRepository.findByIdOrNull(timeSlotId)
    )

    override fun queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId: UUID, studyRoomId: UUID?): List<TimeSlot> {
        return queryFactory.selectFrom(timeSlotJpaEntity)
            .distinct()
            .join(studyRoomTimeSlotJpaEntity)
            .on(
                studyRoomTimeSlotJpaEntity.timeSlot.id.eq(timeSlotJpaEntity.id)
                    .and(studyRoomId?.let { studyRoomTimeSlotJpaEntity.studyRoom.id.eq(it) })
            )
            .join(studyRoomJpaEntity)
            .on(
                studyRoomTimeSlotJpaEntity.studyRoom.id.eq(studyRoomJpaEntity.id),
                studyRoomJpaEntity.school.id.eq(schoolId)
            )
            .fetch()
            .map {
                timeSlotMapper.toDomain(it)!!
            }
    }

    override fun querySeatApplicationsByStudentIdIn(studentIds: List<UUID>): List<StudentSeatApplicationVO> {
        return queryFactory
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

    override fun queryAllSeatTypeBySchoolId(schoolId: UUID) = seatTypeRepository
        .findAllBySchoolId(schoolId).map {
            seatTypeMapper.toDomain(it)!!
        }

    override fun queryAllSeatTypeByStudyRoomId(studyRoomId: UUID) =
        queryFactory
            .select(seatTypeJpaEntity).distinct()
            .from(studyRoomJpaEntity)
            .join(seatJpaEntity).on(studyRoomJpaEntity.id.eq(seatJpaEntity.studyRoom.id))
            .join(seatJpaEntity.type, seatTypeJpaEntity)
            .where(studyRoomJpaEntity.id.eq(studyRoomId))
            .fetch()
            .map {
                seatTypeMapper.toDomain(it)!!
            }

    override fun existsSeatTypeByNameAndSchoolId(name: String, schoolId: UUID): Boolean =
        seatTypeRepository.existsByNameAndSchoolId(name, schoolId)

    override fun querySeatTypeById(seatTypeId: UUID) = seatTypeMapper.toDomain(
        seatTypeRepository.findByIdOrNull(seatTypeId)
    )

    override fun queryAvailableTimeBySchoolId(schoolId: UUID) = availableTimeMapper.toDomain(
        availableTimeRepository.findByIdOrNull(schoolId)
    )

    override fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID) =
        studyRoomRepository.existsByNameAndFloorAndSchoolId(name, floor, schoolId)

    override fun existsSeatBySeatTypeId(seatTypeId: UUID) = seatRepository.existsByTypeId(seatTypeId)

    override fun existsSeatApplicationBySeatIdAndTimeSlotId(seatId: UUID, timeSlotId: UUID) =
        queryFactory.selectFrom(seatApplicationJpaEntity)
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

    override fun existsTimeSlotByStartTimeAndEndTimeAndSchoolId(
        startTime: LocalTime,
        endTime: LocalTime,
        schoolId: UUID
    ): Boolean =
        timeSlotRepository.existsByStartTimeAndEndTimeAndSchoolId(startTime, endTime, schoolId)

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

    override fun saveSeatType(seatType: SeatType) = seatTypeMapper.toDomain(
        seatTypeRepository.save(
            seatTypeMapper.toEntity(seatType)
        )
    )!!

    override fun saveAllStudyRoomTimeSlots(studyRoomTimeSlots: List<StudyRoomTimeSlot>) =
        studyRoomTimeSlotRepository.saveAll(
            studyRoomTimeSlots.map { studyRoomTimeSlotMapper.toEntity(it) }
        ).map { studyRoomTimeSlotMapper.toDomain(it)!! }

    override fun saveAvailableTime(availableTime: AvailableTime) = availableTimeMapper.toDomain(
        availableTimeRepository.save(
            availableTimeMapper.toEntity(availableTime)
        )
    )!!

    override fun deleteSeatType(seatType: SeatType) = seatTypeRepository.delete(
        seatTypeMapper.toEntity(seatType)
    )

    override fun deleteStudyRoomById(studyRoomId: UUID) {
        studyRoomRepository.deleteById(studyRoomId)
    }

    override fun deleteTimeSlotById(timeSlotId: UUID) {
        timeSlotRepository.deleteById(timeSlotId)
    }

    override fun deleteSeatApplicationByStudentId(studentId: UUID) {
        seatApplicationRepository.deleteByStudentId(studentId)
    }

    override fun deleteSeatApplicationByStudentIdAndSeatIdAndTimeSlotId(studentId: UUID, seatId: UUID, timeSlotId: UUID) {
        seatApplicationRepository.deleteByStudentIdAndSeatIdAndTimeSlotId(studentId, seatId, timeSlotId)
    }

    override fun deleteSeatApplicationByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID) {
        seatApplicationRepository.deleteByStudentIdAndTimeSlotId(studentId, timeSlotId)
    }

    override fun deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(seatId: UUID, studentId: UUID, timeSlotId: UUID) {
        seatApplicationRepository.deleteBySeatIdAndStudentIdAndTimeSlotId(seatId, studentId, timeSlotId)
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
}
