package team.aliens.dms.notice

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.notice.usecase.QueryNoticeDetailsUseCase
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse
import team.aliens.dms.domain.notice.usecase.QueryAllNoticesUseCase
import team.aliens.dms.domain.notice.usecase.QueryNoticeStatusUseCase
import team.aliens.dms.domain.notice.usecase.RemoveNoticeUseCase
import team.aliens.dms.notice.dto.request.OrderType
import team.aliens.dms.notice.dto.response.GetNoticeStatusResponse
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import javax.validation.constraints.NotBlank
import java.util.UUID

@Validated
@RequestMapping("/notices")
@RestController
class NoticeWebAdapter(
    private val queryNoticeStatusUseCase: QueryNoticeStatusUseCase,
    private val queryNoticeDetailsUseCase: QueryNoticeDetailsUseCase,
    private val queryAllNoticesUseCase: QueryAllNoticesUseCase,
    private val removeNoticeUseCase: RemoveNoticeUseCase
) {

    @GetMapping("/status")
    fun getNoticeStatus(): GetNoticeStatusResponse {
        val result = queryNoticeStatusUseCase.execute()

        return GetNoticeStatusResponse(result)
    }

    @GetMapping("/{notice-id}")
    fun getDetails(@PathVariable("notice-id") noticeId: UUID): QueryNoticeDetailsResponse {
        return queryNoticeDetailsUseCase.execute(noticeId)
    }

    @GetMapping("/")
    fun queryAllNotices(@RequestParam @NotBlank orderType: OrderType): QueryAllNoticesResponse {
        return queryAllNoticesUseCase.execute(orderType.name)
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{notice-id}")
    fun removeNotice(@PathVariable("notice-id") noticeId: UUID) {
        removeNoticeUseCase.execute(noticeId)
    }
}
