package team.aliens.dms.persistence.student.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.common.annotation.EncryptType
import team.aliens.dms.common.annotation.EncryptedColumn
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.persistence.tag.entity.TagJpaEntity
import java.util.UUID

class QueryStudentsWithTagVO @QueryProjection constructor(
    id: UUID,
    @EncryptedColumn(type = EncryptType.SYMMETRIC)
    override val name: String,
    grade: Int,
    classRoom: Int,
    number: Int,
    roomNumber: String,
    profileImageUrl: String,
    sex: Sex,
    tags: List<TagJpaEntity>
) : StudentWithTag(
    id = id,
    name = name,
    grade = grade,
    classRoom = classRoom,
    number = number,
    roomNumber = roomNumber,
    profileImageUrl = profileImageUrl,
    sex = sex,
    tags = tags.map {
        Tag(
            id = it.id!!,
            name = it.name,
            color = it.color,
            schoolId = it.school!!.id!!
        )
    }
)
