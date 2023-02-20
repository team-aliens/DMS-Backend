package team.aliens.dms.domain.point

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.point.dto.GivePointRequest
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.dto.PageWebData
import team.aliens.dms.domain.point.dto.GrantPointRequest
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.dto.request.GrantPointWebRequest
import team.aliens.dms.domain.point.usecase.GrantPointUseCase
import team.aliens.dms.domain.point.usecase.QueryAllPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.QueryPointHistoryUseCase
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/points")
@RestController
class PointWebAdapter(
    private val queryPointHistoryUseCase: QueryPointHistoryUseCase,
    private val grantPointUseCase: GrantPointUseCase
    private val queryAllPointHistoryUseCase: QueryAllPointHistoryUseCase,
) {

    @GetMapping
    fun getPointHistory(@RequestParam @NotNull type: PointRequestType?): QueryPointHistoryResponse {
        return queryPointHistoryUseCase.execute(type!!)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/history")
    fun grantPoint(@RequestBody @Valid webRequest: GrantPointWebRequest) {
        grantPointUseCase.execute(
            GrantPointRequest(
                pointOptionId = webRequest.pointOptionId!!,
                studentIdList = webRequest.studentIdList!!
            )
        )
    }

    @GetMapping("/history")
    fun getAllPointHistory(
        @RequestParam @NotNull type: PointRequestType?,
        @ModelAttribute pageData: PageWebData
    ): QueryAllPointHistoryResponse {
        return queryAllPointHistoryUseCase.execute(
            type = type!!,
            pageData = PageData(pageData.page, pageData.size)
        )
    }
}