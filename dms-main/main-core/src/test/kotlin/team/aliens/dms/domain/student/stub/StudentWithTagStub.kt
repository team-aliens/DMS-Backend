package team.aliens.dms.domain.manager.stub

import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

internal fun createStudentWithTagStub(
    id: UUID = UUID.randomUUID(),
    name: String = "이름",
    grade: Int = 2,
    classRoom: Int = 2,
    number: Int = 1,
    roomNumber: String = "415",
    profileImageUrl: String = "image",
    sex: Sex = Sex.MALE,
    bonusPoint: Int = 0,
    minusPoint: Int = 0,
    tags: List<Tag> = emptyList()
) = StudentWithTag(
    id = id,
    name = name,
    grade = grade,
    classRoom = classRoom,
    number = number,
    roomNumber = roomNumber,
    profileImageUrl = profileImageUrl,
    sex = sex,
    bonusPoint = bonusPoint,
    minusPoint = minusPoint,
    tags = tags
)
