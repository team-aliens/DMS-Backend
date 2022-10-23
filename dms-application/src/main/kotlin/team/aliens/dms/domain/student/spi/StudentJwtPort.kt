package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import java.util.*

interface StudentJwtPort {

    fun receiveToken(userId: UUID, authority: Authority): TokenResponse

}