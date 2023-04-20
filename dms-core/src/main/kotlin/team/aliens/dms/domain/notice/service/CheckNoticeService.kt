package team.aliens.dms.domain.notice.service

import java.time.LocalDate

interface CheckNoticeService {
    fun existsNoticeByDateBetween(from: LocalDate, to: LocalDate): Boolean
}
