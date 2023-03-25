package team.aliens.dms.persistence.student.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.persistence.tag.entity.TagJpaEntity
import java.util.UUID

data class QueryStudentsWithTagVO @QueryProjection constructor(
    val id: UUID,
    val name: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val roomNumber: String,
    val profileImageUrl: String,
    val sex: Sex,
    val tags: List<TagJpaEntity>
)
