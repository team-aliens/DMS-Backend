package team.aliens.dms.domain.manager.dto.request

object AccountIdRegex {
    const val PATTERN = "^[A-Za-z0-9._-]{4,20}\$"
    const val MESSAGE = "4~20자여야 합니다."
}
