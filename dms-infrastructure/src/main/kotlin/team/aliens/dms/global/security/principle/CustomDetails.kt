package team.aliens.dms.global.security.principle

import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

interface CustomDetails {
    val userId: UUID
    val schoolId: UUID
    val authority: Authority
}
