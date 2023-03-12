package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.studyroom.entity.SeatJpaEntity
import team.aliens.dms.persistence.studyroom.repository.SeatTypeJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomInfoJpaRepository

@Component
class SeatMapper(
    private val studyRoomRepository: StudyRoomInfoJpaRepository,
    private val seatTypeRepository: SeatTypeJpaRepository
) : GenericMapper<Seat, SeatJpaEntity> {

    override fun toDomain(entity: SeatJpaEntity?): Seat? {
        return entity?.let {
            Seat(
                id = it.id!!,
                studyRoomId = it.studyRoom!!.id!!,
                typeId = it.type?.id,
                widthLocation = it.widthLocation,
                heightLocation = it.heightLocation,
                number = it.number,
                status = it.status
            )
        }
    }

    override fun toEntity(domain: Seat): SeatJpaEntity {
        val studyRoom = studyRoomRepository.findByIdOrNull(domain.studyRoomId)

        val seatType = domain.typeId?.let {
            seatTypeRepository.findByIdOrNull(it)
        }

        return SeatJpaEntity(
            id = domain.id,
            studyRoom = studyRoom,
            type = seatType,
            widthLocation = domain.widthLocation,
            heightLocation = domain.heightLocation,
            number = domain.number,
            status = domain.status
        )
    }
}
