package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.auth.exception.AuthCodeLimitNotFoundException
import team.aliens.dms.domain.auth.exception.UnverifiedAuthCodeException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.QueryAuthCodeLimitPort
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.dto.SignUpResponse
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
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
    private val commandStudentPort: CommandStudentPort,
    private val queryStudentPort: QueryStudentPort,
    private val userService: UserService,
    private val querySchoolPort: QuerySchoolPort,
    private val queryAuthCodeLimitPort: QueryAuthCodeLimitPort,
    private val securityService: SecurityService,
    private val jwtPort: JwtPort
) {

    fun execute(request: SignUpRequest): SignUpResponse {
        val (
            schoolCode, schoolAnswer, _,
            grade, classRoom, number,
            accountId, password, email, profileImageUrl
        ) = request

        val school = validateSchool(schoolCode, schoolAnswer)

        validateAuthCodeLimit(email)
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

        val student = queryStudentPort.queryStudentBySchoolIdAndGcn(
            schoolId = school.id,
            grade = grade,
            classRoom = classRoom,
            number = number
        ) ?: throw StudentNotFoundException

        if (student.userId != null) {
            throw StudentAlreadyExistsException
        }

        commandStudentPort.saveStudent(
            student.copy(
                userId = user.id,
                profileImageUrl = profileImageUrl ?: Student.PROFILE_IMAGE
            )
        )

        val (accessToken, accessTokenExpiredAt, refreshToken, refreshTokenExpiredAt) = jwtPort.receiveToken(
            userId = user.id, authority = Authority.STUDENT
        )

        val availableFeatures = querySchoolPort.queryAvailableFeaturesBySchoolId(user.schoolId)
            ?: throw FeatureNotFoundException

        return SignUpResponse(
            accessToken = accessToken,
            accessTokenExpiredAt = accessTokenExpiredAt,
            refreshToken = refreshToken,
            refreshTokenExpiredAt = refreshTokenExpiredAt,
            features = availableFeatures.run {
                SignUpResponse.Features(
                    mealService = mealService,
                    noticeService = noticeService,
                    pointService = pointService,
                    studyRoomService = studyRoomService,
                    remainService = remainService
                )
            }
        )
    }

    private fun validateAuthCodeLimit(email: String) {
        val authCodeLimit = queryAuthCodeLimitPort.queryAuthCodeLimitByEmail(email)
            ?: throw AuthCodeLimitNotFoundException

        if (!authCodeLimit.isVerified) {
            throw UnverifiedAuthCodeException
        }
    }

    private fun validateSchool(schoolCode: String, schoolAnswer: String): School {
        val school = querySchoolPort.querySchoolByCode(schoolCode) ?: throw SchoolCodeMismatchException

        /**
         * 학교 확인 질문 답변 검사
         **/
        if (school.answer != schoolAnswer) {
            throw AnswerMismatchException
        }

        return school
    }

    private fun validateUserDuplicated(accountId: String, email: String) {
        userService.checkUserNotExistsByAccountId(accountId)
        userService.checkUserNotExistsByEmail(email)
    }
}
