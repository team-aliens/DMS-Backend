package team.aliens.dms.persistence.volunteer

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.spi.VolunteerPort
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerWithCurrentApplicantVO
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

    override fun queryAllVolunteersWithCurrentApplicantsBySchoolId(schoolId: UUID): List<VolunteerWithCurrentApplicantVO> {
        return queryFactory.selectFrom(volunteerJpaEntity)
            .leftJoin(volunteerApplicationJpaEntity)
            .on(
                volunteerApplicationJpaEntity.volunteer.id.eq(volunteerJpaEntity.id),
                volunteerApplicationJpaEntity.approved.isTrue
            )
            .transform(
                groupBy(volunteerJpaEntity.id)
                    .list(
                        QQueryVolunteerWithCurrentApplicantVO(
                            volunteerJpaEntity.id,
                            volunteerJpaEntity.name,
                            volunteerJpaEntity.content,
                            volunteerJpaEntity.score,
                            volunteerJpaEntity.optionalScore,
                            list(volunteerApplicationJpaEntity.id),
                            volunteerJpaEntity.maxApplicants,
                            volunteerJpaEntity.availableSex,
                            volunteerJpaEntity.availableGrade,
                            volunteerJpaEntity.school.id
                        )
                    )
            )
    }
}
