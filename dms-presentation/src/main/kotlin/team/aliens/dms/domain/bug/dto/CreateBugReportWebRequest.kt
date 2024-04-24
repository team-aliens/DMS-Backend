package team.aliens.dms.domain.bug.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateBugReportWebRequest(

    @field:NotBlank
    @field:Size(max = 300)
    val content: String,

    @field:NotNull
    val bugType: BugType,

    @field:NotNull
    val developmentArea: DevelopmentArea,

    val attachmentUrls: List<String>?
)
