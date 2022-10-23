package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.exception.AuthCodeNotMatchedException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.AnswerNotMatchedException
import team.aliens.dms.domain.school.exception.CodeNotMatchedException
import team.aliens.dms.domain.student.dto.SignupRequest
import team.aliens.dms.domain.student.dto.TokenAndFeaturesResponse
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentJwtPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.global.annotation.UseCase
import java.util.*

/**
 *
 * 학생이 회원가입을 하는 SignupUseCase
 *
 * @author kimbeomjin
 * @date 2022/10/22
 * @version 1.0.0
 **/
@UseCase
class SignupUseCase(
    private val commandStudentPort: CommandStudentPort,
    private val commandUserPort: StudentCommandUserPort,
    private val querySchoolPort: StudentQuerySchoolPort,
    private val queryUserPort: StudentQueryUserPort,
    private val queryAuthCodePort: StudentQueryAuthCodePort,
    private val securityPort: StudentSecurityPort,
    private val jwtPort: StudentJwtPort
) {

    fun execute(request: SignupRequest): TokenAndFeaturesResponse {
        val (schoolCode, schoolAnswer, email, authCode,
            grade, classRoom, number,
            accountId, password, profileImageUrl) = request

        val school = querySchoolPort.querySchoolByCode(schoolCode) ?: throw CodeNotMatchedException

        /*
        학교 확인 질문 답변 검사
         */
        if (school.answer != schoolAnswer) {
            throw AnswerNotMatchedException
        }

        /*
        이메일 중복 검사
         */
        if (queryUserPort.existsByEmail(email)) {
            throw UserEmailExistsException
        }

        /*
        이메일 인증코드 검사
         */
        val authCodeEntity = queryAuthCodePort.queryAuthCodeByEmail(email) ?: throw AuthCodeNotFoundException

        if (authCode != authCodeEntity.code) {
            throw AuthCodeNotMatchedException
        }

        // TODO 학번으로 이름, 호실 조회

        /*
        아이디 중복 검사
         */
        if (queryUserPort.existsByAccountId(accountId)) {
            throw UserAccountIdExistsException
        }

        val user = commandUserPort.saveUser(
            createUser(
                schoolId = school.id,
                accountId = accountId,
                password = password,
                email = email,
                profileImageUrl = profileImageUrl
            )
        )

        val student = Student(
            studentId = user.id,
            roomNumber = 0, // TODO 학번으로 조회한 호실
            schoolId = school.id,
            grade = grade,
            classRoom = classRoom,
            number = number
        )
        commandStudentPort.saveStudent(student)

        val (accessToken, expiredAt, refreshToken) = jwtPort.receiveToken(user.id, Authority.STUDENT)

        return TokenAndFeaturesResponse(
            accessToken = accessToken,
            expiredAt = expiredAt,
            refreshToken = refreshToken,
            features = TokenAndFeaturesResponse.Features(
                // TODO 서비스 관리 테이블 필요
                mealService = true,
                noticeService = true,
                pointService = true,
            )
        )
    }

    private fun createUser(
        schoolId: UUID,
        accountId: String,
        password: String,
        email: String,
        profileImageUrl: String?
    ) = User(
        schoolId = schoolId,
        accountId = accountId,
        password = securityPort.encode(password),
        email = email,
        name = "", // TODO 학번으로 조회한 이름
        profileImageUrl = profileImageUrl,
        createdAt = null,
        deletedAt = null
    )
}