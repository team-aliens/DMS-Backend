package team.aliens.dms.domain.notice.service

import java.time.LocalDate
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notice.spi.QueryNoticePort

@Service
class CheckNoticeServiceImpl(
    private val queryNoticePort: QueryNoticePort
) : CheckNoticeService{
    override fun existsNoticeByDateBetween(from: LocalDate, to: LocalDate): Boolean {
        return queryNoticePort.existsNoticeByDateBetween(from, to)
    }
}