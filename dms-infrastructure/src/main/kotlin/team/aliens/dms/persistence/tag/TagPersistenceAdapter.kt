package team.aliens.dms.persistence.tag

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.TagPort
import team.aliens.dms.persistence.tag.mapper.TagMapper
import team.aliens.dms.persistence.tag.repository.TagJpaRepository
import java.util.UUID

@Component
class TagPersistenceAdapter(
    private val tagRepository: TagJpaRepository,
    private val tagMapper: TagMapper,
    private val queryFactory: JPAQueryFactory
) : TagPort {
    override fun queryTagsBySchoolId(schoolId: UUID): List<Tag> {
        return tagRepository.findBySchoolId(schoolId)
            .map {
                tagMapper.toDomain(it)!!
            }
    }

    override fun queryTagById(tagId: UUID) = tagMapper.toDomain(
        tagRepository.findByIdOrNull(tagId)
    )
}