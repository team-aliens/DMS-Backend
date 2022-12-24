package team.aliens.dms.persistence.studyroom

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.StudyRoomPort
import team.aliens.dms.persistence.studyroom.mapper.SeatMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomMapper
import team.aliens.dms.persistence.studyroom.repository.SeatJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomJpaRepository

@Component
class StudyRoomPersistenceAdapter(
    private val studyRoomMapper: StudyRoomMapper,
    private val seatMapper: SeatMapper,
    private val studyRoomRepository: StudyRoomJpaRepository,
    private val seatRepository: SeatJpaRepository
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
    }

    override fun saveStudyRoom(studyRoom: StudyRoom) = studyRoomMapper.toDomain(
        studyRoomRepository.save(
            studyRoomMapper.toEntity(studyRoom)
        )
    )!!
}