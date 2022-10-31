package team.aliens.dms.persistence.notice

import org.springframework.stereotype.Component
import team.aliens.dms.domain.notice.spi.NoticePort
import team.aliens.dms.persistence.notice.repository.NoticeJpaRepository
import java.time.LocalDate
import java.time.LocalTime

@Component
class NoticePersistenceAdapter(
    private val noticeRepository: NoticeJpaRepository
) : NoticePort {

    override fun existsNoticeByDateBetween(to: LocalDate, from: LocalDate): Boolean {
        val toLocalDateTime = to.atTime(LocalTime.now())
        val fromLocalDateTime = from.atTime(LocalTime.now())

        return noticeRepository.existsByCreatedAtBetween(toLocalDateTime, fromLocalDateTime)
    }
}