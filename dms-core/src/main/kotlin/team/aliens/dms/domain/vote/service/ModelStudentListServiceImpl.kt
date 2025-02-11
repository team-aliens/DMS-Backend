package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.dto.response.ModelStudentListResponse
import team.aliens.dms.domain.vote.spi.ModelStudentListPort
import java.security.InvalidParameterException
import java.time.LocalDate

@Service
class ModelStudentListServiceImpl(
    private val modelStudentListPort: ModelStudentListPort
) : ModelStudentListService {
    override fun getModelStudentList(requestDate: LocalDate): List<ModelStudentListResponse> {

        val firstDayOfMonth = requestDate.withDayOfMonth(1)
        val lastDayOfMonth = requestDate.withDayOfMonth(requestDate.lengthOfMonth())

        val startOfDay = firstDayOfMonth.atStartOfDay()
        val endOfDay = lastDayOfMonth.atTime(23, 59, 59)

        val modelStudentList = modelStudentListPort.findModelStudents(startOfDay, endOfDay)

        if (modelStudentList.isEmpty()) {
            throw NoSuchElementException("학생을 찾을 수 없습니다")
        }

        return modelStudentList.map { student ->
            ModelStudentListResponse(
                id = student.id,
                studentGcn = student.studentGcn,
                studentName = student.studentName,
                studentProfile = student.studentProfile
            )
        }
    }
}
