package team.aliens.dms.persistence.studyroom

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.spi.SeatTypePort
import team.aliens.dms.persistence.studyroom.entity.QSeatJpaEntity.seatJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatTypeJpaEntity.seatTypeJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QStudyRoomJpaEntity.studyRoomJpaEntity
import team.aliens.dms.persistence.studyroom.mapper.SeatTypeMapper
import team.aliens.dms.persistence.studyroom.repository.SeatTypeJpaRepository
import java.util.UUID

@Component
class SeatTypePersistenceAdapter(
    private val seatTypeMapper: SeatTypeMapper,
    private val seatTypeRepository: SeatTypeJpaRepository,
    private val queryFactory: JPAQueryFactory,
) : SeatTypePort {

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
