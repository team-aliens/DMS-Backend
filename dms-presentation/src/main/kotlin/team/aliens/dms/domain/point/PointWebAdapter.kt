package team.aliens.dms.domain.point

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.usecase.QueryPointHistoryUseCase
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/points")
@RestController
class PointWebAdapter(
    private val queryPointHistoryUseCase: QueryPointHistoryUseCase
) {

    @GetMapping
    fun getPointHistory(@RequestParam @NotBlank type: PointRequestType): QueryPointHistoryResponse {
        return queryPointHistoryUseCase.execute(type)
    }
}