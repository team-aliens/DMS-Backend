package team.aliens.dms.persistence.tag

import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.UUID
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.spi.StudentTagPort
import team.aliens.dms.persistence.tag.mapper.StudentTagMapper
import team.aliens.dms.persistence.tag.repository.StudentTagJpaRepository

@Component
class StudentTagPersistenceAdapter(
    private val studentTagRepository: StudentTagJpaRepository,
    private val studentTagMapper: StudentTagMapper,
    private val queryFactory: JPAQueryFactory
) : StudentTagPort {

    override fun deleteStudentTagByTagId(tagId: UUID) {
        studentTagRepository.deleteByTagId(tagId)
    }

}