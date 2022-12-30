package team.aliens.dms.domain.notice

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse
import team.aliens.dms.domain.notice.dto.request.WebOrderType
import team.aliens.dms.domain.notice.dto.response.GetNoticeStatusResponse
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import team.aliens.dms.domain.notice.usecase.CreateNoticeUseCase
import team.aliens.dms.domain.notice.usecase.QueryAllNoticesUseCase
import team.aliens.dms.domain.notice.usecase.QueryNoticeDetailsUseCase
import team.aliens.dms.domain.notice.usecase.QueryNoticeStatusUseCase
import team.aliens.dms.domain.notice.usecase.RemoveNoticeUseCase
import team.aliens.dms.domain.notice.usecase.UpdateNoticeUseCase
import team.aliens.dms.domain.notice.dto.request.UpdateNoticeWebRequest
import team.aliens.dms.domain.notice.dto.request.CreateNoticeWebRequest
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotNull
import team.aliens.dms.domain.notice.dto.response.CreateNoticeResponse
import team.aliens.dms.domain.notice.dto.response.UpdateNoticeResponse

@Validated
@RequestMapping("/notices")
@RestController
class NoticeWebAdapter(
    private val queryNoticeStatusUseCase: QueryNoticeStatusUseCase,
    private val queryNoticeDetailsUseCase: QueryNoticeDetailsUseCase,
    private val queryAllNoticesUseCase: QueryAllNoticesUseCase,
    private val removeNoticeUseCase: RemoveNoticeUseCase,
    private val updateNoticeUseCase: UpdateNoticeUseCase,
    private val createNoticeUseCase: CreateNoticeUseCase
) {

    @GetMapping("/status")
    fun getNoticeStatus(): GetNoticeStatusResponse {
        val result = queryNoticeStatusUseCase.execute()

        return GetNoticeStatusResponse(result)
    }

    @GetMapping("/{notice-id}")
    fun getDetails(@PathVariable("notice-id") @NotNull noticeId: UUID?): QueryNoticeDetailsResponse {
        return queryNoticeDetailsUseCase.execute(noticeId!!)
    }

    @GetMapping
    fun queryAllNotices(@RequestParam("order") @NotNull orderType: WebOrderType?): QueryAllNoticesResponse {
        return queryAllNoticesUseCase.execute(orderType!!.name)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{notice-id}")
    fun removeNotice(@PathVariable("notice-id") @NotNull noticeId: UUID?) {
        removeNoticeUseCase.execute(noticeId!!)
    }

    @PatchMapping("/{notice-id}")
    fun updateNotice(
        @PathVariable("notice-id") @NotNull noticeId: UUID?,
        @RequestBody @Valid request: UpdateNoticeWebRequest
    ): UpdateNoticeResponse {
        return UpdateNoticeResponse(
            updateNoticeUseCase.execute(
                noticeId = noticeId!!,
                title = request.title!!,
                content = request.content!!
            )
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createNotice(@RequestBody @Valid request: CreateNoticeWebRequest): CreateNoticeResponse {
        return CreateNoticeResponse(
            createNoticeUseCase.execute(
                title = request.title!!,
                content = request.content!!
            )
        )
    }
}
