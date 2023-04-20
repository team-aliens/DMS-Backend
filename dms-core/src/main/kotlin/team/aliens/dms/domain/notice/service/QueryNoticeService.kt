package team.aliens.dms.domain.notice.service

import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.model.OrderType
import java.util.UUID

interface QueryNoticeService {
    fun getAllNoticesBySchoolIdAndOrder(schoolId: UUID, orderType: OrderType): List<Notice>

    fun getNoticeById(noticeId: UUID): Notice

    fun queryNoticeByIdAndManagerId(noticeId: UUID, managerId: UUID): Notice
}
