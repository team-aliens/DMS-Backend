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

    override fun querySeatTypeId(seatId: UUID?) = seatId?.run {
        seatTypeMapper.toDomain(
            seatTypeRepository.findByIdOrNull(this)
        )
    }

    override fun saveSeatType(seatType: SeatType) = seatTypeMapper.toDomain(
        seatTypeRepository.save(
            seatTypeMapper.toEntity(seatType)
        )
    )!!
}