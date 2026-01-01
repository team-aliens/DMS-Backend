package team.aliens.dms.persistence.volunteer

import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotFoundException
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.domain.volunteer.spi.VolunteerScorePort
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerScoreWithStudentVO
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.volunteer.entity.QVolunteerScoreJpaEntity.volunteerScoreJpaEntity
import team.aliens.dms.persistence.volunteer.mapper.VolunteerApplicationMapper
import team.aliens.dms.persistence.volunteer.mapper.VolunteerMapper
import team.aliens.dms.persistence.volunteer.mapper.VolunteerScoreMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerApplicationJpaRepository
import team.aliens.dms.persistence.volunteer.repository.VolunteerJpaRepository
import team.aliens.dms.persistence.volunteer.repository.VolunteerScoreRepository
import team.aliens.dms.persistence.volunteer.repository.vo.QQueryVolunteerScoreWithStudentVO
import java.util.UUID

@Component
class VolunteerScorePersistenceAdapter(
    private val volunteerScoreRepository: VolunteerScoreRepository,
    private val volunteerApplicationRepository: VolunteerApplicationJpaRepository,
    private val volunteerRepository: VolunteerJpaRepository,
    private val volunteerScoreMapper: VolunteerScoreMapper,
    private val volunteerApplicationMapper: VolunteerApplicationMapper,
    private val volunteerMapper: VolunteerMapper,
    private val queryFactory: JPAQueryFactory
) : VolunteerScorePort {

    override fun saveVolunteerScore(volunteerScore: VolunteerScore) {
        volunteerScoreRepository.save(
            volunteerScoreMapper.toEntity(volunteerScore)
        )
    }

    override fun updateVolunteerScore(applicationId: UUID, updateScore: Int) {
        val entity = volunteerScoreRepository.findByVolunteerApplicationId(applicationId)
            ?: throw VolunteerApplicationNotFoundException

        entity.assignScore = updateScore

        volunteerScoreRepository.save(entity)
    }

    override fun queryExistsByApplicationId(applicationId: UUID): Boolean {
        return volunteerScoreRepository.existsByVolunteerApplicationId(applicationId)
    }

    override fun queryVolunteerApplicationById(applicationId: UUID): VolunteerApplication? =
        volunteerApplicationRepository.findByIdOrNull(applicationId)
            ?.let { volunteerApplicationMapper.toDomain(it) }

    override fun queryVolunteerById(volunteerId: UUID): Volunteer? =
        volunteerRepository.findByIdOrNull(volunteerId)
            ?.let { volunteerMapper.toDomain(it) }

    override fun queryAllVolunteerScoresWithStudentVO(): List<VolunteerScoreWithStudentVO> {
        val gcnExpression = Expressions.stringTemplate(
            "CONCAT({0}, {1}, LPAD(CAST({2} AS CHAR), 2, '0'))",
            studentJpaEntity.grade,
            studentJpaEntity.classRoom,
            studentJpaEntity.number
        )

        return queryFactory
            .select(
                QQueryVolunteerScoreWithStudentVO(
                    studentJpaEntity.id,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    volunteerScoreJpaEntity.assignScore,
                    CaseBuilder()
                        .`when`(pointHistoryJpaEntity.pointType.eq(PointType.BONUS))
                        .then(pointHistoryJpaEntity.pointScore)
                        .otherwise(0)
                        .sum()
                        .coalesce(0),
                    CaseBuilder()
                        .`when`(pointHistoryJpaEntity.pointType.eq(PointType.MINUS))
                        .then(pointHistoryJpaEntity.pointScore)
                        .otherwise(0)
                        .sum()
                        .coalesce(0),
                    studentJpaEntity.schoolId
                )
            )
            .from(volunteerScoreJpaEntity)
            .join(volunteerScoreJpaEntity.volunteerApplication)
            .join(studentJpaEntity).on(volunteerScoreJpaEntity.volunteerApplication.studentId.eq(studentJpaEntity.id))
            .leftJoin(pointHistoryJpaEntity).on(
                pointHistoryJpaEntity.studentGcn.eq(gcnExpression),
                pointHistoryJpaEntity.isCancel.eq(false)
            )
            .groupBy(
                studentJpaEntity.id,
                studentJpaEntity.name,
                studentJpaEntity.grade,
                studentJpaEntity.classRoom,
                studentJpaEntity.number,
                volunteerScoreJpaEntity.assignScore,
                studentJpaEntity.schoolId
            )
            .fetch()
            .map { it.toDomain() }
    }

    override fun deleteAllVolunteerScores() {
        volunteerScoreRepository.deleteAll()
    }

    override fun queryScoreByApplicationId(applicationId: UUID): VolunteerScore? =
        volunteerScoreRepository.findByVolunteerApplicationId(applicationId)
            ?.let { volunteerScoreMapper.toDomain(it) }
}
