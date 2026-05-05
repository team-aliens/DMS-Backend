package team.aliens.dms.persistence.teacher

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.teacher.spi.TeacherPort
import team.aliens.dms.domain.teacher.spi.vo.TeacherVO
import team.aliens.dms.persistence.teacher.entity.QTeacherJpaEntity.teacherJpaEntity
import team.aliens.dms.persistence.teacher.mapper.TeacherMapper
import team.aliens.dms.persistence.teacher.repository.TeacherJpaRepository
import team.aliens.dms.persistence.teacher.repository.vo.QQueryTeacherVO
import java.util.UUID

@Component
class TeacherPersistenceAdapter(
    private val teacherRepository: TeacherJpaRepository,
    private val teacherMapper: TeacherMapper,
    private val queryFactory: JPAQueryFactory
) : TeacherPort {

    override fun getTeacherById(id: UUID) =
        teacherMapper.toDomain(teacherRepository.findByIdOrNull(id))

    override fun getAllTeachersBySchoolIdAndAuthority(
        schoolId: UUID,
        authority: Authority
    ): List<TeacherVO> {
        return queryFactory
            .select(
                QQueryTeacherVO(
                    teacherJpaEntity.id,
                    teacherJpaEntity.name
                )
            )
            .from(teacherJpaEntity)
            .where(teacherJpaEntity.user.authority.eq(authority))
            .fetch()
    }
}
