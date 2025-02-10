package team.aliens.dms.domain.notice.service

import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.model.OrderType
import java.util.UUID

interface GetNoticeService {

    fun getAllNoticesBySchoolIdAndOrder(schoolId: UUID, orderType: OrderType): List<Notice>

    fun getNoticeById(noticeId: UUID): Notice

    fun getNoticeByIdAndManagerId(noticeId: UUID, managerId: UUID): Notice
}
