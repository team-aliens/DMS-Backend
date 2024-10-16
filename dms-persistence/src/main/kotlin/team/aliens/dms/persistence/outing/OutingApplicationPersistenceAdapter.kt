package team.aliens.dms.persistence.outing

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions.selectOne
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.spi.OutingApplicationPort
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.persistence.outing.entity.QOutingApplicationJpaEntity.outingApplicationJpaEntity
import team.aliens.dms.persistence.outing.entity.QOutingCompanionJpaEntity.outingCompanionJpaEntity
import team.aliens.dms.persistence.outing.entity.QOutingTypeJpaEntity.outingTypeJpaEntity
import team.aliens.dms.persistence.outing.mapper.OutingApplicationMapper
import team.aliens.dms.persistence.outing.repository.OutingApplicationJpaRepository
import team.aliens.dms.persistence.outing.repository.OutingCompanionJpaRepository
import team.aliens.dms.persistence.outing.repository.vo.QQueryCurrentOutingApplicationVO
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingApplicationVO
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingCompanionVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity
import java.time.LocalDate
import java.util.UUID

@Component
class OutingApplicationPersistenceAdapter(
    private val outingApplicationMapper: OutingApplicationMapper,
    private val outingApplicationRepository: OutingApplicationJpaRepository,
    private val outingCompanionsRepository: OutingCompanionJpaRepository,
    private val queryFactory: JPAQueryFactory
) : OutingApplicationPort {

    override fun existOutingApplicationByOutingDateAndStudentId(outingDate: LocalDate, studentId: UUID) =
        outingApplicationRepository.existsByOutingDateAndStudentId(outingDate, studentId)

    override fun queryOutingApplicationById(outingApplicationId: UUID) =
        outingApplicationMapper.toDomain(
            outingApplicationRepository.findByIdOrNull(outingApplicationId)
        )

    override fun queryAllOutingApplicationVOsBetweenStartAndEnd(
        name: String?,
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
            .where(
                containsName(name),
                outingApplicationJpaEntity.outingDate.between(start, end)
            )
            .orderBy(outingApplicationJpaEntity.outingDate.asc())
            .transform(
                groupBy(outingApplicationJpaEntity.id)
                    .list(
                        QQueryOutingApplicationVO(
                            outingApplicationJpaEntity.id,
                            studentJpaEntity.name,
                            studentJpaEntity.grade,
                            studentJpaEntity.classRoom,
                            studentJpaEntity.number,
                            outingApplicationJpaEntity.outingType.id.title,
                            outingApplicationJpaEntity.outingDate,
                            outingApplicationJpaEntity.outingTime,
                            outingApplicationJpaEntity.arrivalTime,
                            outingApplicationJpaEntity.isApproved,
                            outingApplicationJpaEntity.isReturned,
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
                outingApplicationJpaEntity.student.id.eq(studentId)
                    .or(
                        selectOne()
                            .from(outingApplicationJpaEntity)
                            .leftJoin(outingCompanionJpaEntity).on(outingApplicationJpaEntity.id.eq(outingCompanionJpaEntity.outingApplication.id))
                            .where(
                                outingCompanionJpaEntity.student.id.eq(studentId),
                                outingApplicationJpaEntity.student.id.eq(studentJpaEntity.id),
                                outingApplicationJpaEntity.isReturned.eq(false)
                            )
                            .exists()
                    ),
                outingApplicationJpaEntity.isReturned.eq(false)
            )
            .transform(
                groupBy(outingApplicationJpaEntity.id)
                    .list(
                        QQueryCurrentOutingApplicationVO(
                            outingApplicationJpaEntity.id,
                            outingApplicationJpaEntity.outingDate,
                            outingTypeJpaEntity.id.title,
                            outingApplicationJpaEntity.outingTime,
                            outingApplicationJpaEntity.arrivalTime,
                            outingApplicationJpaEntity.reason,
                            outingApplicationJpaEntity.student.name,
                            list(outingCompanionJpaEntity.student.name)
                        )
                    )
            ).firstOrNull()
    }

    override fun saveOutingApplication(outingApplication: OutingApplication) =
        outingApplicationMapper.toDomain(
            outingApplicationRepository.save(
                outingApplicationMapper.toEntity(outingApplication)
            )
        )!!

    override fun deleteOutingApplication(outingApplication: OutingApplication) {
        outingCompanionsRepository.deleteAllByOutingApplication(
            outingApplicationMapper.toEntity(outingApplication)
        )

        outingApplicationRepository.delete(
            outingApplicationMapper.toEntity(outingApplication)
        )
    }

    override fun isApplicant(studentId: UUID): Boolean {
        val studentJpaEntity = QStudentJpaEntity("studentJpaEntity")

        val result = queryFactory
            .select(studentJpaEntity.id)
            .from(outingApplicationJpaEntity)
            .join(outingApplicationJpaEntity.student, studentJpaEntity)
            .where(
                studentJpaEntity.id.eq(studentId),
                outingApplicationJpaEntity.isReturned.eq(false)
            )
            .fetchOne()

        return result != null
    }

    private fun containsName(name: String?): BooleanExpression? {
        return name?.let { outingApplicationJpaEntity.student.name.contains(it) }
    }
}
