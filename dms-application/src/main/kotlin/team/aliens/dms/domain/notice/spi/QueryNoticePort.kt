package team.aliens.dms.domain.notice.spi

import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.model.OrderType
import java.time.LocalDate
import java.util.UUID

interface QueryNoticePort {

    fun existsNoticeByDateBetween(to: LocalDate, from: LocalDate): Boolean

    fun queryNoticeById(noticeId: UUID): Notice?

    fun queryAllNoticesBySchoolIdAndOrder(schoolId: UUID, orderType: OrderType): List<Notice>

    fun queryNoticeByIdAndManagerId(noticeId: UUID, managerId: UUID): Notice?

}
