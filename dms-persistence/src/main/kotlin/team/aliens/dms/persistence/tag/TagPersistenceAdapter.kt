package team.aliens.dms.persistence.tag

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.TagPort
import team.aliens.dms.persistence.tag.entity.QStudentTagJpaEntity.studentTagJpaEntity
import team.aliens.dms.persistence.tag.entity.QTagJpaEntity.tagJpaEntity
import team.aliens.dms.persistence.tag.mapper.TagMapper
import team.aliens.dms.persistence.tag.repository.TagJpaRepository
import java.util.UUID

@Component
class TagPersistenceAdapter(
    private val tagRepository: TagJpaRepository,
    private val tagMapper: TagMapper,
    private val queryFactory: JPAQueryFactory
) : TagPort {

    override fun queryTagsByTagNameIn(names: List<String>): List<Tag> {
        return queryFactory.selectFrom(tagJpaEntity)
            .where(
                tagJpaEntity.name.`in`(names)
            ).fetch()
            .map {
                tagMapper.toDomain(it)!!
            }
    }

    override fun queryTagsBySchoolId(schoolId: UUID): List<Tag> {
        return tagRepository.findBySchoolId(schoolId)
            .map {
                tagMapper.toDomain(it)!!
            }
    }

    override fun queryTagById(tagId: UUID): Tag? {
        return tagMapper.toDomain(
            tagRepository.findByIdOrNull(tagId)
        )
    }

    override fun queryTagByName(name: String): Tag? {
        return tagMapper.toDomain(
            tagRepository.findByName(name)
        )
    }

    override fun deleteTagById(tagId: UUID) {
        tagRepository.deleteById(tagId)
    }

    override fun existsByNameAndSchoolId(name: String, schoolId: UUID): Boolean {
        return tagRepository.existsByNameAndSchoolId(name, schoolId)
    }

    override fun saveTag(tag: Tag): Tag {
        return tagMapper.toDomain(
            tagRepository.save(tagMapper.toEntity(tag))
        )!!
    }

    override fun queryTagsByStudentId(studentId: UUID): List<Tag> {
        return queryFactory
            .selectFrom(tagJpaEntity)
            .join(studentTagJpaEntity)
            .on(
                studentTagJpaEntity.student.id.eq(studentId),
                studentTagJpaEntity.tag.id.eq(tagJpaEntity.id)
            )
            .fetch()
            .map {
                tagMapper.toDomain(it)!!
            }
    }
}
