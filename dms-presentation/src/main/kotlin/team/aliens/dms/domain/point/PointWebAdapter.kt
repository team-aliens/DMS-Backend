package team.aliens.dms.domain.point

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServerHttpResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.dto.PageWebData
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.usecase.ImportAllPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.QueryAllPointHistoryUseCase
import team.aliens.dms.domain.point.usecase.QueryPointHistoryUseCase
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/points")
@RestController
class PointWebAdapter(
    private val queryPointHistoryUseCase: QueryPointHistoryUseCase,
    private val queryAllPointHistoryUseCase: QueryAllPointHistoryUseCase,
    private val importAllPointHistoryUseCase: ImportAllPointHistoryUseCase
) {

    @GetMapping
    fun getPointHistory(@RequestParam @NotNull type: PointRequestType?): QueryPointHistoryResponse {
        return queryPointHistoryUseCase.execute(type!!)
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
    @GetMapping("/history/excel")
    fun importAllPointHistory(
        httpResponse: ServerHttpResponse,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: LocalDateTime?,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: LocalDateTime?
    ): ByteArray {
        val response = importAllPointHistoryUseCase.execute(start, end)
        httpResponse.headers.add(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=${response.fileName}.xlsx"
        )
        return response.file
    }
}