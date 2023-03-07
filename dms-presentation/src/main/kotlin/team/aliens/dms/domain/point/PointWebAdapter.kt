package team.aliens.dms.domain.point

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.dto.PageWebData
import team.aliens.dms.domain.point.dto.CreatePointOptionRequest
import team.aliens.dms.domain.point.dto.CreatePointOptionResponse
import team.aliens.dms.domain.point.dto.GrantPointRequest
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.dto.QueryPointOptionsResponse
import team.aliens.dms.domain.point.dto.QueryStudentPointHistoryResponse
import team.aliens.dms.domain.point.dto.request.CreatePointOptionWebRequest
import team.aliens.dms.domain.point.dto.request.GrantPointWebRequest
import team.aliens.dms.domain.point.dto.request.UpdatePointOptionWebRequest
import team.aliens.dms.domain.point.usecase.CancelGrantedPointUseCase
import team.aliens.dms.domain.point.usecase.CreatePointOptionUseCase
import team.aliens.dms.domain.point.usecase.ExportAllPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.GrantPointUseCase
import team.aliens.dms.domain.point.usecase.QueryAllPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.QueryPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.QueryPointOptionsUseCase
import team.aliens.dms.domain.point.usecase.QueryStudentPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.RemovePointOptionUseCase
import team.aliens.dms.domain.point.usecase.UpdatePointOptionUseCase
import java.net.URLEncoder
import java.time.LocalDateTime
import java.util.UUID
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/points")
@RestController
class PointWebAdapter(
    private val queryPointHistoryUseCase: QueryPointHistoryUseCase,
    private val createPointOptionUseCase: CreatePointOptionUseCase,
    private val removePointOptionUseCase: RemovePointOptionUseCase,
    private val grantPointUseCase: GrantPointUseCase,
    private val queryAllPointHistoryUseCase: QueryAllPointHistoryUseCase,
    private val exportAllPointHistoryUseCase: ExportAllPointHistoryUseCase,
    private val cancelGrantedPointUseCase: CancelGrantedPointUseCase,
    private val queryPointOptionsUseCase: QueryPointOptionsUseCase,
    private val queryStudentPointHistoryUseCase: QueryStudentPointHistoryUseCase,
    private val updatePointOptionUseCase: UpdatePointOptionUseCase
) {

    @GetMapping
    fun getPointHistory(
        @RequestParam @NotNull type: PointRequestType?,
        @ModelAttribute pageData: PageData
    ): QueryPointHistoryResponse {
        return queryPointHistoryUseCase.execute(type!!, pageData)
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
    fun removePointOption(@PathVariable(name = "point-option-id") @NotNull pointOptionId: UUID?) {
        removePointOptionUseCase.execute(pointOptionId!!)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/options/{point-option-id}")
    fun updatePointOption(
        @PathVariable(name = "point-option-id") @NotNull pointOptionId: UUID?,
        @RequestBody @Valid webRequest: UpdatePointOptionWebRequest
    ) {
        updatePointOptionUseCase.execute(
            pointOptionId = pointOptionId!!,
            name = webRequest.name!!,
            score = webRequest.score!!
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/history")
    fun grantPoint(@RequestBody @Valid webRequest: GrantPointWebRequest) {
        grantPointUseCase.execute(
            GrantPointRequest(
                pointOptionId = webRequest.pointOptionId!!,
                studentIdList = webRequest.studentIdList!!.map {
                    it ?: throw ConstraintViolationException(setOf())
                }
            )
        )
    }

    @GetMapping("/history")
    fun getPointHistories(
        @RequestParam @NotNull type: PointRequestType?,
        @ModelAttribute pageData: PageWebData
    ): QueryAllPointHistoryResponse {
        return queryAllPointHistoryUseCase.execute(
            type = type!!,
            pageData = PageData(pageData.page, pageData.size)
        )
    }

    @GetMapping("/history/file")
    fun exportAllPointHistory(
        httpResponse: HttpServletResponse,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: LocalDateTime?,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: LocalDateTime?
    ): ByteArray {
        val response = exportAllPointHistoryUseCase.execute(start, end)
        httpResponse.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=${URLEncoder.encode(response.fileName, "UTF-8")}.xlsx"
        )
        return response.file
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/history/{point-history-id}")
    fun cancelGrantedPoint(@PathVariable("point-history-id") @NotNull pointHistoryId: UUID?) {
        cancelGrantedPointUseCase.execute(pointHistoryId!!)
    }

    @GetMapping("/options")
    fun getAllPointOptions(@RequestParam(required = false) keyword: String?): QueryPointOptionsResponse {
        return queryPointOptionsUseCase.execute(keyword)
    }

    @GetMapping("/history/students/{student-id}")
    fun getStudentsPointHistory(
        @PathVariable("student-id") @NotNull studentId: UUID?,
        @ModelAttribute pageData: PageData
    ): QueryStudentPointHistoryResponse {
        return queryStudentPointHistoryUseCase.execute(studentId!!, pageData)
    }
}
