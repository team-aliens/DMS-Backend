package team.aliens.dms.persistence.tag

import com.querydsl.core.Tuple
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.spi.StudentTagPort
import team.aliens.dms.persistence.tag.mapper.StudentTagMapper
import team.aliens.dms.persistence.tag.repository.StudentTagJpaRepository
import java.util.UUID
import team.aliens.dms.persistence.tag.entity.QStudentTagJpaEntity.studentTagJpaEntity

@Component
class StudentTagPersistenceAdapter(
    private val studentTagRepository: StudentTagJpaRepository,
    private val studentTagMapper: StudentTagMapper,
    private val queryFactory: JPAQueryFactory
) : StudentTagPort {

    override fun deleteStudentTagByTagId(tagId: UUID) {
        studentTagRepository.deleteByTagId(tagId)
    }

    override fun existsByTagIdAndStudentIds(tagId: UUID, studentIds: List<UUID>): Boolean {
        return queryFactory
            .selectFrom(studentTagJpaEntity)
            .where(
                Expressions.list(studentTagJpaEntity.student, studentTagJpaEntity.tag)
                    .`in`(*queryStudentTagIn(tagId, studentIds))
            ).fetchFirst() != null
    }

    private fun queryStudentTagIn(tagId: UUID, studentIdList: List<UUID>): Array<Expression<Tuple>> {
        val tuple: MutableList<Expression<Tuple>> = ArrayList()
        for (studentId in studentIdList) {
            tuple.add(
                Expressions.template(
                    Tuple::class.java,
                    "(({0}, {1}))",
                    tagId, studentId
                )
            )
        }

        return tuple.toTypedArray()
    }
}
