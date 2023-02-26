package team.aliens.dms.domain.user.service

import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

interface CheckUserAuthority {

    fun execute(userId: UUID): Authority
}
