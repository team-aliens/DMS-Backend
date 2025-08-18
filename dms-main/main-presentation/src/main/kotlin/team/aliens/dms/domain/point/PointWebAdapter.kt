package team.aliens.dms.domain.point

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
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
import team.aliens.dms.common.extension.setExcelContentDisposition
import team.aliens.dms.domain.point.dto.AllPointHistoryResponse
import team.aliens.dms.domain.point.dto.CreatePointOptionRequest
import team.aliens.dms.domain.point.dto.CreatePointOptionResponse
import team.aliens.dms.domain.point.dto.GrantPointRequest
import team.aliens.dms.domain.point.dto.PointHistoryResponse
import team.aliens.dms.domain.point.dto.PointOptionsResponse
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.StudentPointHistoryResponse
import team.aliens.dms.domain.point.dto.request.CreatePointOptionWebRequest
import team.aliens.dms.domain.point.dto.request.GrantPointWebRequest
import team.aliens.dms.domain.point.dto.request.UpdatePointOptionWebRequest
import team.aliens.dms.domain.point.usecase.CancelGrantedPointUseCase
import team.aliens.dms.domain.point.usecase.CreatePointOptionUseCase
import team.aliens.dms.domain.point.usecase.ExportAllPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.ExportPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.GrantPointUseCase
import team.aliens.dms.domain.point.usecase.QueryAllPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.QueryPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.QueryPointOptionsUseCase
import team.aliens.dms.domain.point.usecase.QueryStudentPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.QueryStudentRecentPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.RemovePointOptionUseCase
import team.aliens.dms.domain.point.usecase.UpdatePointOptionUseCase
import java.time.LocalDateTime
import java.util.UUID

@Validated
@RequestMapping("/points")
@RestController
class PointWebAdapter(
    private val queryPointHistoryUseCase: QueryPointHistoryUseCase,
    private val createPointOptionUseCase: CreatePointOptionUseCase,
    private val removePointOptionUseCase: RemovePointOptionUseCase,
    private val grantPointUseCase: GrantPointUseCase,
    private val queryAllPointHistoryUseCase: QueryAllPointHistoryUseCase,
    private val queryStudentRecentPointHistoryUseCase: QueryStudentRecentPointHistoryUseCase,
    private val exportAllPointHistoryUseCase: ExportAllPointHistoryUseCase,
    private val exportPointHistoryUseCase: ExportPointHistoryUseCase,
    private val cancelGrantedPointUseCase: CancelGrantedPointUseCase,
    private val queryPointOptionsUseCase: QueryPointOptionsUseCase,
    private val queryStudentPointHistoryUseCase: QueryStudentPointHistoryUseCase,
    private val updatePointOptionUseCase: UpdatePointOptionUseCase
) {

    @GetMapping
    fun getPointHistory(
        @RequestParam @NotNull type: PointRequestType,
        @ModelAttribute pageData: PageData
    ): PointHistoryResponse {
        return queryPointHistoryUseCase.execute(type, pageData)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/options")
    fun createPointOption(@RequestBody @Valid request: CreatePointOptionWebRequest): CreatePointOptionResponse {
        return createPointOptionUseCase.execute(
            CreatePointOptionRequest(
                name = request.name,
                score = request.score,
                type = request.type.name
            )
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/options/{point-option-id}")
    fun removePointOption(@PathVariable(name = "point-option-id") @NotNull pointOptionId: UUID) {
        removePointOptionUseCase.execute(pointOptionId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/options/{point-option-id}")
    fun updatePointOption(
        @PathVariable(name = "point-option-id") @NotNull pointOptionId: UUID,
        @RequestBody @Valid webRequest: UpdatePointOptionWebRequest
    ) {
        updatePointOptionUseCase.execute(
            pointOptionId = pointOptionId,
            name = webRequest.name,
            score = webRequest.score
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/history")
    fun grantPoint(@RequestBody @Valid webRequest: GrantPointWebRequest) {
        grantPointUseCase.execute(
            GrantPointRequest(
                pointOptionId = webRequest.pointOptionId,
                studentIdList = webRequest.studentIdList
            )
        )
    }

    @GetMapping("/history")
    fun getPointHistories(
        @RequestParam @NotNull type: PointRequestType,
        @ModelAttribute pageData: PageData
    ): AllPointHistoryResponse {
        return queryAllPointHistoryUseCase.execute(
            type = type,
            pageData = PageData(pageData.page, pageData.size)
        )
    }

    @GetMapping("/history/file")
    fun exportAllPointHistory(
        httpResponse: HttpServletResponse,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: LocalDateTime
    ): ByteArray {
        val response = exportAllPointHistoryUseCase.execute(start, end)
        httpResponse.setExcelContentDisposition(response.fileName)
        return response.file
    }

    @GetMapping("/history/excel")
    fun exportPointHistory(
        httpResponse: HttpServletResponse
    ): ByteArray {
        val response = exportPointHistoryUseCase.execute()
        httpResponse.setExcelContentDisposition(response.fileName)
        return response.file
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/history/{point-history-id}")
    fun cancelGrantedPoint(@PathVariable("point-history-id") @NotNull pointHistoryId: UUID) {
        cancelGrantedPointUseCase.execute(pointHistoryId)
    }

    @GetMapping("/options")
    fun getAllPointOptions(@RequestParam(required = false) keyword: String?): PointOptionsResponse {
        return queryPointOptionsUseCase.execute(keyword)
    }

    @GetMapping("/history/students/{student-id}")
    fun getStudentsPointHistory(
        @PathVariable("student-id") @NotNull studentId: UUID,
        @ModelAttribute pageData: PageData
    ): PointHistoryResponse {
        return queryStudentPointHistoryUseCase.execute(studentId, pageData)
    }

    @GetMapping("/history/students/{student-id}/recent")
    fun getStudentRecentPointHistory(
        @PathVariable("student-id") @NotNull studentId: UUID
    ): StudentPointHistoryResponse {
        return queryStudentRecentPointHistoryUseCase.execute(studentId)
    }
}
