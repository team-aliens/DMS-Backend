package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.auth.dto.TokenFeatureResponse
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.service.AuthService
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.service.UserService

/**
 *
 * 학생이 회원가입을 하는 SignUpUseCase
 *
 * @author kimbeomjin, leejeongyoon
 * @date 2022/10/22
 * @version 1.0.0
 **/
@UseCase
class SignUpUseCase(
    private val studentService: StudentService,
    private val userService: UserService,
    private val schoolService: SchoolService,
    private val authService: AuthService,
    private val securityService: SecurityService,
    private val jwtPort: JwtPort
) {

    fun execute(request: SignUpRequest): TokenFeatureResponse {
        val (
            schoolCode, schoolAnswer, _,
            grade, classRoom, number,
            accountId, password, email, profileImageUrl
        ) = request

        val school = validateSchool(schoolCode, schoolAnswer)

        authService.checkAuthCodeLimitIsVerifiedByEmail(email)
        validateUserDuplicated(accountId, email)

        val user = userService.saveUser(
            User(
                schoolId = school.id,
                accountId = accountId,
                password = securityService.encodePassword(password),
                email = email,
                authority = Authority.STUDENT
            )
        )

        val student = studentService.getStudentBySchoolIdAndGcn(
            schoolId = school.id,
            grade = grade,
            classRoom = classRoom,
            number = number
        )

        if (student.hasUser) {
            throw StudentAlreadyExistsException
        }

        studentService.saveStudent(
            student.copy(
                userId = user.id,
                profileImageUrl = profileImageUrl ?: Student.PROFILE_IMAGE
            )
        )

        val token = jwtPort.receiveToken(
            userId = user.id, authority = Authority.STUDENT, schoolId = school.id,
        )
        val availableFeatures = schoolService.getAvailableFeaturesBySchoolId(user.schoolId)

        return TokenFeatureResponse.of(token, availableFeatures)
    }

    private fun validateSchool(schoolCode: String, schoolAnswer: String) =
        schoolService.getSchoolByCode(schoolCode)
            .apply { checkAnswer(schoolAnswer) }

    private fun validateUserDuplicated(accountId: String, email: String) {
        userService.checkUserNotExistsByAccountId(accountId)
        userService.checkUserNotExistsByEmail(email)
    }
}
