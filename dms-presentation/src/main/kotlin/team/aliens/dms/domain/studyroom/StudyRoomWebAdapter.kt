package team.aliens.dms.domain.studyroom

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.studyroom.dto.QueryStudyRoomAvailableTimeResponse

@RequestMapping("/study-rooms")
@RestController
class StudyRoomWebAdapter(
    private val queryStudyRoomAvailableTimeUseCase: QueryStudyRoomAvailableTimeUseCase
) {

    @GetMapping("/available-time")
    fun getAvailableTime(): QueryStudyRoomAvailableTimeResponse {
        return queryStudyRoomAvailableTimeUseCase.execute()
    }
}