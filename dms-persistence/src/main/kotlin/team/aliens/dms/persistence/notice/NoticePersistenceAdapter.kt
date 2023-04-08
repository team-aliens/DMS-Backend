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

    override fun existsNoticeByDateBetween(from: LocalDate, to: LocalDate): Boolean {
        val fromLocalDateTime = from.atTime(LocalTime.now())
        val toLocalDateTime = to.atTime(LocalTime.now())

        return noticeRepository.existsByCreatedAtBetween(fromLocalDateTime, toLocalDateTime)
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

    override fun queryNoticeByIdAndManagerId(noticeId: UUID, managerId: UUID) = noticeMapper.toDomain(
        noticeRepository.findByIdAndManagerUserId(noticeId, managerId)
    )

    override fun deleteNotice(notice: Notice) {
        noticeRepository.delete(
            noticeMapper.toEntity(notice)
        )
    }

    override fun saveNotice(notice: Notice) = noticeMapper.toDomain(
        noticeRepository.save(
            noticeMapper.toEntity(notice)
        )
    )!!
}
