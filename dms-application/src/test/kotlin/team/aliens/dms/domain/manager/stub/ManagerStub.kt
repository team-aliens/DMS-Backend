package team.aliens.dms.domain.manager.stub

import team.aliens.dms.domain.manager.model.Manager
import java.util.UUID

internal fun createManagerStub(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    name: String = "관리자 이름",
    profileImageUrl: String? = "https://~~"
) = Manager(
    id = id,
    schoolId = schoolId,
    name = name,
    profileImageUrl = profileImageUrl
)