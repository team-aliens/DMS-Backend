package team.aliens.dms.common.model

import java.io.Serializable
import java.util.UUID

data class SchoolSecret(
    val schoolId: UUID,
    val secretKey: String
) : Serializable
