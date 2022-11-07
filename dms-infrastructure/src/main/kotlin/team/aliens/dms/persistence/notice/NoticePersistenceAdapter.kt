package team.aliens.dms.persistence.notice

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.spi.NoticePort
import team.aliens.dms.persistence.notice.mapper.NoticeMapper
import team.aliens.dms.persistence.notice.repository.NoticeJpaRepository
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Component
class NoticePersistenceAdapter(
    private val noticeRepository: NoticeJpaRepository,
    private val noticeMapper: NoticeMapper
) : NoticePort {

    override fun existsNoticeByDateBetween(to: LocalDate, from: LocalDate): Boolean {
        val toLocalDateTime = to.atTime(LocalTime.now())
        val fromLocalDateTime = from.atTime(LocalTime.now())

        return noticeRepository.existsByCreatedAtBetween(toLocalDateTime, fromLocalDateTime)
    }

    override fun queryNoticeById(noticeId: UUID) = noticeMapper.toDomain(
        noticeRepository.findByIdOrNull(noticeId)
    )

    override fun queryAllNoticesBySchoolIdAndOrder(schoolId: UUID, orderType: OrderType): List<Notice> {
        return when (orderType) {
            OrderType.NEW -> noticeRepository.findAllByManagerUserSchoolIdOrderByCreatedAtDesc(schoolId)
            OrderType.OLD -> noticeRepository.findAllByManagerUserSchoolIdOrderByCreatedAtAsc(schoolId)
        }.map {
            noticeMapper.toDomain(it)!!
        }
    }

    override fun deleteNotice(notice: Notice) {
        noticeRepository.delete(
            noticeMapper.toEntity(notice)
        )
    }
}