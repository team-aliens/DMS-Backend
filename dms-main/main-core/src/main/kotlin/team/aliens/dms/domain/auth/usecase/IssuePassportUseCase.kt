package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.auth.model.Passport
import team.aliens.dms.domain.auth.service.AuthService

@ReadOnlyUseCase
class IssuePassportUseCase(
    private val authService: AuthService
) {

    fun execute(token: String): Passport {
        return authService.getPassportByToken(token)
    }
}
