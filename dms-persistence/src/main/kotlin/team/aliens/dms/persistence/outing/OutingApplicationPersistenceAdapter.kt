package team.aliens.dms.persistence.outing

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.spi.OutingApplicationPort
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import team.aliens.dms.persistence.outing.entity.QOutingApplicationJpaEntity.outingApplicationJpaEntity
import team.aliens.dms.persistence.outing.entity.QOutingCompanionJpaEntity.outingCompanionJpaEntity
import team.aliens.dms.persistence.outing.entity.QOutingTypeJpaEntity.outingTypeJpaEntity
import team.aliens.dms.persistence.outing.mapper.OutingApplicationMapper
import team.aliens.dms.persistence.outing.repository.OutingApplicationJpaRepository
import team.aliens.dms.persistence.outing.repository.vo.QQueryCurrentOutingApplicationVO
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingApplicationVO
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingCompanionVO
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingHistoryVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity
import java.time.LocalDate
import java.util.UUID

@Component
class OutingApplicationPersistenceAdapter(
    private val outingApplicationMapper: OutingApplicationMapper,
    private val outingApplicationRepository: OutingApplicationJpaRepository,
    private val queryFactory: JPAQueryFactory
) : OutingApplicationPort {

    override fun existOutingApplicationByOutingDateAndStudentId(outingDate: LocalDate, studentId: UUID) =
        outingApplicationRepository.existsByOutingDateAndStudentId(outingDate, studentId)

    override fun queryOutingApplicationById(outingApplicationId: UUID) =
        outingApplicationMapper.toDomain(
            outingApplicationRepository.findByIdOrNull(outingApplicationId)
        )

    override fun queryAllOutingApplicationVOsBetweenStartAndEnd(
        start: LocalDate,
        end: LocalDate
    ): List<OutingApplicationVO> {
        val studentJpaEntity = QStudentJpaEntity("studentJpaEntity")
        val outingCompanionStudentJpaEntity = QStudentJpaEntity("outingCompanionStudentJpaEntity")

        return queryFactory
            .selectFrom(outingApplicationJpaEntity)
            .join(outingApplicationJpaEntity.student, studentJpaEntity)
            .leftJoin(outingCompanionJpaEntity)
            .on(outingApplicationJpaEntity.id.eq(outingCompanionJpaEntity.outingApplication.id))
            .leftJoin(outingCompanionJpaEntity.student, outingCompanionStudentJpaEntity)
            .where(outingApplicationJpaEntity.outingDate.between(start, end))
            .orderBy(outingApplicationJpaEntity.outingDate.asc())
            .transform(
                groupBy(outingApplicationJpaEntity.id)
                    .list(
                        QQueryOutingApplicationVO(
                            studentJpaEntity.name,
                            studentJpaEntity.grade,
                            studentJpaEntity.classRoom,
                            studentJpaEntity.number,
                            outingApplicationJpaEntity.outingDate,
                            outingApplicationJpaEntity.outingTime,
                            outingApplicationJpaEntity.arrivalTime,
                            list(
                                QQueryOutingCompanionVO(
                                    outingCompanionStudentJpaEntity.name,
                                    outingCompanionStudentJpaEntity.grade,
                                    outingCompanionStudentJpaEntity.classRoom,
                                    outingCompanionStudentJpaEntity.number
                                )
                            )
                        )
                    )
            )
    }

    override fun queryCurrentOutingApplicationVO(studentId: UUID): CurrentOutingApplicationVO? {
        val studentJpaEntity = QStudentJpaEntity("studentJpaEntity")
        val outingCompanionStudentJpaEntity = QStudentJpaEntity("outingCompanionStudentJpaEntity")

        return queryFactory
            .selectFrom(outingApplicationJpaEntity)
            .leftJoin(outingCompanionJpaEntity)
            .on(outingApplicationJpaEntity.id.eq(outingCompanionJpaEntity.outingApplication.id))
            .leftJoin(outingCompanionJpaEntity.student, outingCompanionStudentJpaEntity)
            .join(outingApplicationJpaEntity.student, studentJpaEntity)
            .join(outingApplicationJpaEntity.outingType, outingTypeJpaEntity)
            .where(
                studentJpaEntity.id.eq(studentId),
                outingApplicationJpaEntity.status.ne(OutingStatus.DONE)
            )
            .transform(
                groupBy(outingApplicationJpaEntity.id)
                    .list(
                        QQueryCurrentOutingApplicationVO(
                            outingApplicationJpaEntity.outingDate,
                            outingTypeJpaEntity.id.title,
                            outingApplicationJpaEntity.status,
                            outingApplicationJpaEntity.outingTime,
                            outingApplicationJpaEntity.arrivalTime,
                            outingApplicationJpaEntity.reason,
                            list(outingCompanionStudentJpaEntity.name)
                        )
                    )
            ).firstOrNull()
    }

    override fun queryOutingHistoriesByStudentNameAndDate(studentName: String?, date: LocalDate): List<OutingHistoryVO> {
        val studentJpaEntity = QStudentJpaEntity("studentJpaEntity")
        val outingCompanionStudentJpaEntity = QStudentJpaEntity("outingCompanionStudentJpaEntity")

        return queryFactory
            .select(
                QQueryOutingHistoryVO(
                    outingApplicationJpaEntity.id,
                    studentJpaEntity.name,
                    outingTypeJpaEntity.id.title,
                    outingCompanionJpaEntity.count().intValue(),
                    outingApplicationJpaEntity.status,
                    outingApplicationJpaEntity.outingTime,
                    outingApplicationJpaEntity.arrivalTime
                )
            )
            .from(outingApplicationJpaEntity)
            .join(outingApplicationJpaEntity.student, studentJpaEntity)
            .join(outingApplicationJpaEntity.outingType, outingTypeJpaEntity)
            .leftJoin(outingCompanionJpaEntity)
            .on(outingApplicationJpaEntity.id.eq(outingCompanionJpaEntity.outingApplication.id))
            .leftJoin(outingCompanionJpaEntity.student, outingCompanionStudentJpaEntity)
            .where(
                studentName?.let { studentJpaEntity.name.eq(it) },
                outingApplicationJpaEntity.outingDate.eq(date)
            )
            .groupBy(outingApplicationJpaEntity.id)
            .orderBy(outingApplicationJpaEntity.outingDate.asc())
            .fetch()
    }

    override fun saveOutingApplication(outingApplication: OutingApplication) =
        outingApplicationMapper.toDomain(
            outingApplicationRepository.save(
                outingApplicationMapper.toEntity(outingApplication)
            )
        )!!

    override fun deleteOutingApplication(outingApplication: OutingApplication) {
        outingApplicationRepository.delete(
            outingApplicationMapper.toEntity(outingApplication)
        )
    }
}
