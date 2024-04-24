package team.aliens.dms.domain.bug

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.bug.dto.CreateBugReportRequest
import team.aliens.dms.domain.bug.dto.CreateBugReportWebRequest
import team.aliens.dms.domain.bug.usecase.CreateBugReportUseCase

@Validated
@RequestMapping("/bugs")
@RestController
class BugWebAdapter(
    private val createBugReportUseCase: CreateBugReportUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createBugReport(@RequestBody @Valid request: CreateBugReportWebRequest) {
        createBugReportUseCase.execute(
            CreateBugReportRequest(
                content = request.content,
                type = request.bugType.name,
                developmentArea = request.developmentArea.name,
                attachmentUrls = request.attachmentUrls
            )
        )
    }
}
