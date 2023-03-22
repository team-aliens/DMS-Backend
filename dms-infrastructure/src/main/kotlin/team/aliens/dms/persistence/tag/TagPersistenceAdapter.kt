package team.aliens.dms.persistence.tag

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.spi.TagPort
import team.aliens.dms.persistence.tag.repository.TagJpaRepository

@Component
class TagPersistenceAdapter(
    private val tagRepository: TagJpaRepository,
    private val queryFactory: JPAQueryFactory
) : TagPort {

}