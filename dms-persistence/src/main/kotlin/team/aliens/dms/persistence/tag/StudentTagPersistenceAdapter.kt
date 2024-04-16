package team.aliens.dms.persistence.tag

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.spi.StudentTagPort
import team.aliens.dms.persistence.tag.entity.QStudentTagJpaEntity.studentTagJpaEntity
import team.aliens.dms.persistence.tag.entity.StudentTagId
import team.aliens.dms.persistence.tag.mapper.StudentTagMapper
import team.aliens.dms.persistence.tag.repository.StudentTagJpaRepository
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

    override fun deleteAllStudentTagsByTagIdIn(tagIds: List<UUID>) {
        queryFactory.delete(studentTagJpaEntity)
            .where(
                studentTagJpaEntity.tag.id.`in`(tagIds)
            ).execute()
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
