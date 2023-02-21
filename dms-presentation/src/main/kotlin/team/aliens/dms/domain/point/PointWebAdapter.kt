package team.aliens.dms.domain.point

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.dto.PageWebData
import team.aliens.dms.domain.point.dto.GrantPointRequest
import team.aliens.dms.domain.point.dto.CreatePointOptionRequest
import team.aliens.dms.domain.point.dto.CreatePointOptionResponse
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.dto.QueryPointOptionsResponse
import team.aliens.dms.domain.point.dto.request.GrantPointWebRequest
import team.aliens.dms.domain.point.usecase.CancelGrantedPointUseCase
import team.aliens.dms.domain.point.usecase.GrantPointUseCase
import team.aliens.dms.domain.point.usecase.QueryAllPointHistoryUseCase
import team.aliens.dms.domain.point.dto.request.CreatePointOptionWebRequest
import team.aliens.dms.domain.point.usecase.CreatePointOptionUseCase
import team.aliens.dms.domain.point.usecase.RemovePointOptionUseCase
import team.aliens.dms.domain.point.usecase.QueryPointHistoryUseCase
import java.util.UUID
import team.aliens.dms.domain.point.usecase.QueryPointOptionsUseCase
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/points")
@RestController
class PointWebAdapter(
    private val queryPointHistoryUseCase: QueryPointHistoryUseCase,
    private val createPointOptionUseCase: CreatePointOptionUseCase,
    private val grantPointUseCase: GrantPointUseCase,
    private val queryAllPointHistoryUseCase: QueryAllPointHistoryUseCase,
    private val cancelGrantedPointUseCase: CancelGrantedPointUseCase,
    private val removePointOptionUseCase: RemovePointOptionUseCase
    private val queryPointOptionsUseCase: QueryPointOptionsUseCase
) {

    @GetMapping
    fun getPointHistory(@RequestParam @NotNull type: PointRequestType?): QueryPointHistoryResponse {
        return queryPointHistoryUseCase.execute(type!!)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/options")
    fun createPointOption(@RequestBody @Valid request: CreatePointOptionWebRequest): CreatePointOptionResponse {
        return createPointOptionUseCase.execute(
                CreatePointOptionRequest(
                    name = request.name!!,
                    score = request.score!!,
                    type = request.type!!.name
                )
            )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/options/{point-option-id}")
    fun removePointOption(@PathVariable(name = "point-option-id") pointOptionId: UUID) {
        removePointOptionUseCase.execute(pointOptionId)
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/history/{point-history-id}")
    fun cancelGrantedPoint(@PathVariable("point-history-id") pointHistoryId: UUID) {
        cancelGrantedPointUseCase.execute(pointHistoryId)
    }

    @GetMapping("/options")
    fun getAllPointOptions(@RequestParam(required = false) keyword: String?): QueryPointOptionsResponse {
        return queryPointOptionsUseCase.execute(keyword)
    }
}