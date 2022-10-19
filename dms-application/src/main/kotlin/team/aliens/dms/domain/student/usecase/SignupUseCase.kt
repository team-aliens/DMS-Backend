package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.student.dto.SignupRequest
import team.aliens.dms.domain.student.dto.TokenAndFeaturesResponse
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.CommandUserPort
import team.aliens.dms.global.annotation.UseCase
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class SignupUseCase(
    private val commandStudentPort: CommandStudentPort,
    private val commandUserPort: CommandUserPort
) {

    fun execute(request: SignupRequest): TokenAndFeaturesResponse {
        val schoolCode = request.schoolCode
        val schoolAnswer = request.schoolAnswer

        val student = Student(
            studentId = UUID(0, 0),
            roomNumber = 1,
            schoolId = UUID(0, 0),
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number
        )

        val user = User(
            id = UUID(0, 0),
            schoolId = UUID(0, 0),
            accountId = request.accountId,
            password = request.password,
            email = request.email,
            name = "ss",
            profileImageUrl = "2",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
        return TODO()
    }
}