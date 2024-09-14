package team.aliens.dms.persistence.volunteer

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.VolunteerApplicationPort
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.volunteer.entity.QVolunteerApplicationJpaEntity.volunteerApplicationJpaEntity
import team.aliens.dms.persistence.volunteer.entity.QVolunteerJpaEntity.volunteerJpaEntity
import team.aliens.dms.persistence.volunteer.mapper.VolunteerApplicationMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerApplicationJpaRepository
import team.aliens.dms.persistence.volunteer.repository.vo.QQueryCurrentVolunteerApplicantVO
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
            .join(studentJpaEntity, volunteerApplicationJpaEntity.student)
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

    override fun queryAllApplicantsBySchoolIdGroupByVolunteer(schoolId: UUID): List<CurrentVolunteerApplicantVO> {
        return queryFactory
            .selectFrom(volunteerApplicationJpaEntity)
            .join(volunteerApplicationJpaEntity.volunteer, volunteerJpaEntity)
            .join(volunteerApplicationJpaEntity.student, studentJpaEntity)
            .where(
                volunteerJpaEntity.school.id.eq(schoolId),
                volunteerApplicationJpaEntity.approved.eq(true)
            )
            .transform(
                groupBy(volunteerJpaEntity.name)
                    .list(
                        QQueryCurrentVolunteerApplicantVO(
                            volunteerJpaEntity.name,
                            list(
                                QQueryVolunteerApplicantVO(
                                    volunteerApplicationJpaEntity.id,
                                    studentJpaEntity.grade,
                                    studentJpaEntity.classRoom,
                                    studentJpaEntity.number,
                                    studentJpaEntity.name,
                                )
                            )
                        )
                    )
            )
    }
}
