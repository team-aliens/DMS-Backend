package team.aliens.dms.persistence.studyroom

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.spi.SeatTypePort
import team.aliens.dms.persistence.studyroom.mapper.SeatTypeMapper
import team.aliens.dms.persistence.studyroom.repository.SeatTypeJpaRepository

@Component
class SeatTypePersistenceAdapter(
    private val seatTypeMapper: SeatTypeMapper,
    private val seatTypeRepository: SeatTypeJpaRepository
) : SeatTypePort {

    override fun queryAllSeatTypeBySchoolId(schoolId: UUID) = seatTypeRepository
        .findAllBySchoolId(schoolId).map {
            seatTypeMapper.toDomain(it)!!
        }

    override fun existsSeatTypeByName(name: String) = seatTypeRepository.existsByName(name)

    override fun querySeatTypeById(seatTypeId: UUID) = seatTypeMapper.toDomain(
        seatTypeRepository.findByIdOrNull(seatTypeId)
    )

    override fun saveSeatType(seatType: SeatType) = seatTypeMapper.toDomain(
        seatTypeRepository.save(
            seatTypeMapper.toEntity(seatType)
        )
    )!!

    override fun deleteSeatType(seatType: SeatType) = seatTypeRepository.delete(
        seatTypeMapper.toEntity(seatType)
    )
}