package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import java.time.LocalDate

@ReadOnlyUseCase
class QueryNoticeStatusUseCase(
    private val queryNoticePort: QueryNoticePort
) {

    /**
     * 7일 이내의 공지사항 유무 조회
     **/
    fun execute(): Boolean {
        val now = LocalDate.now()
        val from = now.minusDays(NOTICE_PERIOD)

        return queryNoticePort.existsNoticeByDateBetween(from, now)
    }

    companion object {
        private const val NOTICE_PERIOD: Long = 7
    }
}
