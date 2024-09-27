package team.aliens.dms.persistence.volunteer

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.VolunteerApplicationPort
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.volunteer.entity.QVolunteerApplicationJpaEntity.volunteerApplicationJpaEntity
import team.aliens.dms.persistence.volunteer.entity.QVolunteerJpaEntity.volunteerJpaEntity
import team.aliens.dms.persistence.volunteer.mapper.VolunteerApplicationMapper
import team.aliens.dms.persistence.volunteer.mapper.VolunteerMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerApplicationJpaRepository
import team.aliens.dms.persistence.volunteer.repository.vo.QQueryCurrentVolunteerApplicantVO
import team.aliens.dms.persistence.volunteer.repository.vo.QQueryVolunteerApplicantVO
import java.util.UUID

@Component
class VolunteerApplicationPersistenceAdapter(
    private val volunteerApplicationMapper: VolunteerApplicationMapper,
    private val volunteerApplicationRepository: VolunteerApplicationJpaRepository,
    private val queryFactory: JPAQueryFactory,
    private val volunteerMapper: VolunteerMapper
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
            .join(volunteerApplicationJpaEntity.student, studentJpaEntity)
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

    override fun deleteAllVolunteerApplicationsByVolunteerId(volunteerId: UUID) {
        volunteerApplicationRepository.deleteAllByVolunteerId(volunteerId)
    }

    override fun queryAllApplicantsBySchoolIdGroupByVolunteer(schoolId: UUID): List<CurrentVolunteerApplicantVO> {
        return queryFactory
            .selectFrom(volunteerJpaEntity)
            .leftJoin(volunteerApplicationJpaEntity)
            .on(
                volunteerApplicationJpaEntity.volunteer.id.eq(volunteerJpaEntity.id)
                    .and(volunteerApplicationJpaEntity.approved.isTrue)
            )
            .leftJoin(studentJpaEntity)
            .on(studentJpaEntity.id.eq(volunteerApplicationJpaEntity.student.id))
            .where(
                volunteerJpaEntity.school.id.eq(schoolId)
            )
            .transform(
                groupBy(volunteerJpaEntity.id)
                    .list(
                        QQueryCurrentVolunteerApplicantVO(
                            volunteerJpaEntity.name,
                            volunteerJpaEntity.availableSex,
                            volunteerJpaEntity.availableGrade,
                            list(volunteerApplicationJpaEntity.id),
                            volunteerJpaEntity.maxApplicants,
                            list(
                                QQueryVolunteerApplicantVO(
                                    volunteerApplicationJpaEntity.id,
                                    studentJpaEntity.grade,
                                    studentJpaEntity.classRoom,
                                    studentJpaEntity.number,
                                    studentJpaEntity.name,
                                ).skipNulls()
                            )
                        )
                    )
            )
    }

    override fun queryVolunteerApplicationByStudentIdAndVolunteerId(
        studentId: UUID,
        volunteerId: UUID
    ): VolunteerApplication? {
        return volunteerApplicationMapper.toDomain(
            volunteerApplicationRepository.findByStudentIdAndVolunteerId(
                studentId, volunteerId
            )
        )
    }

    override fun getVolunteerApplicationsWithVolunteersByStudentId(studentId: UUID): List<Pair<VolunteerApplication, Volunteer>> {
        val result = queryFactory
            .select(volunteerApplicationJpaEntity, volunteerJpaEntity)
            .from(volunteerApplicationJpaEntity)
            .join(volunteerApplicationJpaEntity.volunteer, volunteerJpaEntity)
            .where(volunteerApplicationJpaEntity.student.id.eq(studentId))
            .fetch()

        return result.map { tuple ->
            val volunteerApplication = volunteerApplicationMapper.toDomain(tuple[volunteerApplicationJpaEntity])!!
            val volunteer = volunteerMapper.toDomain(tuple[volunteerJpaEntity])!!

            volunteerApplication to volunteer
        }
    }
}
