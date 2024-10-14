package team.aliens.dms.persistence.volunteer

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.set
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.spi.VolunteerPort
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerWithCurrentApplicantVO
import team.aliens.dms.persistence.volunteer.entity.QVolunteerApplicationJpaEntity
import team.aliens.dms.persistence.volunteer.entity.QVolunteerApplicationJpaEntity.volunteerApplicationJpaEntity
import team.aliens.dms.persistence.volunteer.entity.QVolunteerJpaEntity.volunteerJpaEntity
import team.aliens.dms.persistence.volunteer.mapper.VolunteerMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerJpaRepository
import team.aliens.dms.persistence.volunteer.repository.vo.QQueryVolunteerWithCurrentApplicantVO
import java.util.UUID

@Component
class VolunteerPersistenceAdapter(
    private val volunteerMapper: VolunteerMapper,
    private val volunteerJpaRepository: VolunteerJpaRepository,
    private val queryFactory: JPAQueryFactory
) : VolunteerPort {

    override fun saveVolunteer(volunteer: Volunteer): Volunteer = volunteerMapper.toDomain(
        volunteerJpaRepository.save(
            volunteerMapper.toEntity(volunteer)
        )
    )!!

    override fun deleteVolunteer(volunteer: Volunteer) {
        volunteerJpaRepository.delete(
            volunteerMapper.toEntity(volunteer)
        )
    }

    override fun queryVolunteerById(volunteerId: UUID): Volunteer? = volunteerMapper.toDomain(
        volunteerJpaRepository.findByIdOrNull(volunteerId)
    )

    override fun queryAllVolunteersBySchoolId(schoolId: UUID): List<Volunteer> {
        return volunteerJpaRepository.findAllBySchoolId(schoolId)
            .map { volunteerMapper.toDomain(it)!! }
    }

    override fun queryAllVolunteersWithCurrentApplicantsBySchoolIdAndStudentId(schoolId: UUID, studentId: UUID?): List<VolunteerWithCurrentApplicantVO> {
        val myApplication = QVolunteerApplicationJpaEntity("myApplication")

        return queryFactory.selectFrom(volunteerJpaEntity)
            .leftJoin(volunteerApplicationJpaEntity)
            .on(
                volunteerApplicationJpaEntity.volunteer.id.eq(volunteerJpaEntity.id),
                volunteerApplicationJpaEntity.approved.isTrue
            )
            .leftJoin(myApplication)
            .on(
                myApplication.volunteer.id.eq(volunteerJpaEntity.id),
                studentId?.let { myApplication.student.id.eq(it) }
                    ?: Expressions.asBoolean(true).isTrue
            )
            .where(volunteerJpaEntity.school.id.eq(schoolId))
            .transform(
                groupBy(volunteerJpaEntity.id)
                    .list(
                        QQueryVolunteerWithCurrentApplicantVO(
                            volunteerJpaEntity.id,
                            volunteerJpaEntity.name,
                            volunteerJpaEntity.score,
                            volunteerJpaEntity.optionalScore,
                            set(volunteerApplicationJpaEntity.id),
                            volunteerJpaEntity.maxApplicants,
                            volunteerJpaEntity.availableSex,
                            volunteerJpaEntity.availableGrade,
                            volunteerJpaEntity.school.id,
                            myApplication.approved
                        )
                    )
            )
    }
}
