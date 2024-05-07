package team.aliens.dms.persistence.tag

import com.querydsl.jpa.JPAExpressions.selectFrom
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.spi.StudentTagPort
import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.tag.entity.QStudentTagJpaEntity.studentTagJpaEntity
import team.aliens.dms.persistence.tag.entity.QTagJpaEntity.tagJpaEntity
import team.aliens.dms.persistence.tag.entity.StudentTagId
import team.aliens.dms.persistence.tag.mapper.StudentTagMapper
import team.aliens.dms.persistence.tag.repository.StudentTagJpaRepository
import team.aliens.dms.persistence.tag.repository.vo.QQueryStudentTagDetailVO
import java.util.UUID

@Component
class StudentTagPersistenceAdapter(
    private val studentTagRepository: StudentTagJpaRepository,
    private val studentTagMapper: StudentTagMapper,
    private val queryFactory: JPAQueryFactory
) : StudentTagPort {

    override fun queryStudentTagsByStudentId(studentId: UUID): List<StudentTag> {
        return studentTagRepository.findAllByStudentId(studentId)
            .map { studentTagMapper.toDomain(it)!! }
    }

    override fun queryAllStudentTagDetails(): List<StudentTagDetailVO> {
        return queryFactory.select(
            QQueryStudentTagDetailVO(
                studentJpaEntity.id,
                studentJpaEntity.name,
                tagJpaEntity.id,
                tagJpaEntity.color,
                tagJpaEntity.name
            )
        ).from(studentTagJpaEntity)
            .join(tagJpaEntity).on(studentTagJpaEntity.tag.id.eq(tagJpaEntity.id))
            .join(studentJpaEntity).on(studentTagJpaEntity.student.id.eq(studentJpaEntity.id))
            .fetch()
    }

    override fun queryStudentTagsByTagNameIn(names: List<String>): List<StudentTag> {
        return queryFactory.selectFrom(studentTagJpaEntity)
            .join(tagJpaEntity).on(tagJpaEntity.id.eq(studentTagJpaEntity.tag.id))
            .where(tagJpaEntity.name.`in`(names))
            .fetch()
            .map {
                studentTagMapper.toDomain(it)!!
            }
    }

    override fun deleteAllStudentTagsByStudentIdIn(studentIds: List<UUID>) {
        queryFactory.delete(studentTagJpaEntity)
            .where(studentTagJpaEntity.student.id.`in`(studentIds))
            .execute()
    }

    override fun deleteStudentTagById(studentId: UUID, tagId: UUID) {
        val id = StudentTagId(
            studentId = studentId,
            tagId = tagId
        )

        studentTagRepository.deleteById(id)
    }

    override fun deleteStudentTagByTagId(tagId: UUID) {
        studentTagRepository.deleteByTagId(tagId)
    }

    override fun saveStudentTag(studentTag: StudentTag) = studentTagMapper.toDomain(
        studentTagRepository.save(
            studentTagMapper.toEntity(studentTag)
        )
    )!!

    override fun saveAllStudentTags(studentTags: List<StudentTag>) {
        studentTagRepository.saveAll(
            studentTags.map {
                studentTagMapper.toEntity(it)
            }
        )
    }
}
