package team.aliens.dms.domain.point.stub

import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType
import java.util.UUID

internal fun createPhraseStub(
    id: UUID = UUID.randomUUID(),
    content: String = "내용",
    type: PointType = PointType.BONUS,
    standard: Int = 0
) = Phrase(
    id = id,
    content = content,
    type = type,
    standard = standard
)