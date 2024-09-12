package team.aliens.dms.persistence.volunteer

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.VolunteerApplicationPort
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.volunteer.entity.QVolunteerApplicationJpaEntity.volunteerApplicationJpaEntity
import team.aliens.dms.persistence.volunteer.mapper.VolunteerApplicationMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerApplicationJpaRepository
import team.aliens.dms.persistence.volunteer.repository.vo.QQueryVolunteerApplicantVO
import java.util.UUID

@Component
class VolunteerApplicationPersistenceAdapter(
    private val volunteerApplicationMapper: VolunteerApplicationMapper,
    private val volunteerApplicationRepository: VolunteerApplicationJpaRepository,
    private val queryFactory: JPAQueryFactory
) : VolunteerApplicationPort {

    override fun queryVolunteerApplicationById(volunteerApplicationId: UUID) =
        volunteerApplicationMapper.toDomain(
            volunteerApplicationRepository.findByIdOrNull(volunteerApplicationId)
        )

    override fun queryAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO> {
        return queryFactory
            .select(
                QQueryVolunteerApplicantVO(
                    volunteerApplicationJpaEntity.id,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    studentJpaEntity.name,
                )
            )
            .from(volunteerApplicationJpaEntity)
            .leftJoin(studentJpaEntity)
            .where(
                volunteerApplicationJpaEntity.volunteer.id.eq(volunteerId),
                volunteerApplicationJpaEntity.approved.eq(false)
            )
            .fetch()
    }

    override fun saveVolunteerApplication(volunteerApplication: VolunteerApplication) =
        volunteerApplicationMapper.toDomain(
            volunteerApplicationRepository.save(
                volunteerApplicationMapper.toEntity(volunteerApplication)
            )
        )!!

    override fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication) {
        volunteerApplicationRepository.delete(
            volunteerApplicationMapper.toEntity(volunteerApplication)
        )
    }

    override fun queryVolunteerApplicationsByStudentId(studentId: UUID) =
        volunteerApplicationRepository.findByStudentId(studentId)
            .mapNotNull { volunteerApplicationMapper.toDomain(it) }
}
