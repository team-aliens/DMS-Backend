package team.aliens.dms.persistence.studyroom

import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.spi.SeatTypePort
import team.aliens.dms.persistence.school.entity.QSchoolJpaEntity.schoolJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatJpaEntity.seatJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatTypeJpaEntity.seatTypeJpaEntity
import team.aliens.dms.persistence.studyroom.mapper.SeatTypeMapper
import team.aliens.dms.persistence.user.entity.QUserJpaEntity.userJpaEntity

@Component
class SeatPersistenceAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val seatTypeMapper: SeatTypeMapper
) : SeatTypePort {

    override fun queryAllSeatTypeByUserId(userId: UUID): List<SeatType> {
        return jpaQueryFactory.selectFrom(seatTypeJpaEntity)
            .join(userJpaEntity).on(userJpaEntity.id.eq(userId))
            .join(seatJpaEntity).on(seatTypeJpaEntity.id.eq(seatJpaEntity.type.id))
            .join(seatJpaEntity.studyRoom.school, schoolJpaEntity)
            .fetch()
            .map {
                seatTypeMapper.toDomain(it)!!
            }
    }
}