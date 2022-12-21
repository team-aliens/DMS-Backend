package team.aliens.dms.persistence.studyroom

import java.util.UUID
import org.springframework.stereotype.Component
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
}