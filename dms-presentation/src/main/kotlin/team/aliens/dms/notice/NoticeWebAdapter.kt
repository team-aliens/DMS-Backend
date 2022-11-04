package team.aliens.dms.notice

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse
import team.aliens.dms.domain.notice.usecase.QueryAllNoticesUseCase
import team.aliens.dms.domain.notice.usecase.QueryNoticeStatusUseCase
import team.aliens.dms.notice.dto.request.OrderType
import team.aliens.dms.notice.dto.response.GetNoticeStatusResponse
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/notices")
@RestController
class NoticeWebAdapter(
    private val queryNoticeStatusUseCase: QueryNoticeStatusUseCase,
    private val queryAllNoticesUseCase: QueryAllNoticesUseCase
) {

    @GetMapping("/status")
    fun getNoticeStatus(): GetNoticeStatusResponse {
        val result = queryNoticeStatusUseCase.execute()

        return GetNoticeStatusResponse(result)
    }

    @GetMapping("/")
    fun queryAllNotices(@RequestParam @NotBlank orderType: OrderType): QueryAllNoticesResponse {
        return queryAllNoticesUseCase.execute(orderType.name)
    }
}
