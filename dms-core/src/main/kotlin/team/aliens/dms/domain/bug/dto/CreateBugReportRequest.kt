package team.aliens.dms.domain.bug.dto

data class CreateBugReportRequest(
    val content: String,
    val developmentArea: String,
    val attachmentUrls: List<String>?
)
