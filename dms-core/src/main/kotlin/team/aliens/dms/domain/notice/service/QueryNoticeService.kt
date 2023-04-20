package team.aliens.dms.domain.notice.service

import java.util.UUID
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.model.OrderType

interface QueryNoticeService {
    fun getAllNoticesBySchoolIdAndOrder(schoolId: UUID, orderType: OrderType): List<Notice>

    fun getNoticeById(noticeId: UUID): Notice

    fun queryNoticeByIdAndManagerId(noticeId: UUID, managerId: UUID): Notice
}