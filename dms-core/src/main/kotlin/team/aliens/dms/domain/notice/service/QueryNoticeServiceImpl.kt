package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import java.util.UUID

@Service
class QueryNoticeServiceImpl(
    private val queryNoticePort: QueryNoticePort
) : QueryNoticeService {
    override fun getAllNoticesBySchoolIdAndOrder(schoolId: UUID, orderType: OrderType): List<Notice> {
        return queryNoticePort.queryAllNoticesBySchoolIdAndOrder(schoolId, orderType)
    }

    override fun getNoticeById(noticeId: UUID): Notice {
        return queryNoticePort.queryNoticeById(noticeId)
            ?: throw NoticeNotFoundException
    }

    override fun queryNoticeByIdAndManagerId(noticeId: UUID, managerId: UUID): Notice {
        return queryNoticePort.queryNoticeByIdAndManagerId(noticeId, managerId)
            ?: throw NoticeNotFoundException
    }
}
