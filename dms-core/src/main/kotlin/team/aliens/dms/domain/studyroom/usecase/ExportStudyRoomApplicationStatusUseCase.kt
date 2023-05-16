package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.studyroom.dto.ExportStudyRoomApplicationStatusResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportStudyRoomApplicationStatusUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService,
    private val schoolService: SchoolService
) {

    fun execute(file: java.io.File?): ExportStudyRoomApplicationStatusResponse {

        val user = userService.getCurrentUser()

        val timeSlots = studyRoomService.getTimeSlots(user.schoolId)
        val studentSeats = studyRoomService.getStudentSeatInfos(user.schoolId)
        val school = schoolService.getSchoolById(user.schoolId)

        return ExportStudyRoomApplicationStatusResponse(
            file = studyRoomService.getStudyRoomApplicationStatusFile(
                file = file,
                timeSlots = timeSlots,
                studentSeats = studentSeats
            ),
            fileName = getFileName(school.name)
        )
    }

    private fun getFileName(schoolName: String) =
        "${schoolName.replace(" ", "")}_자습실_신청상태_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}
