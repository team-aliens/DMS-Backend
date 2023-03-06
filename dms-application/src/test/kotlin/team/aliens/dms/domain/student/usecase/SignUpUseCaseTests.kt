package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.exception.VerifiedStudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentJwtPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQueryRoomPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentQueryVerifiedStudentPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class SignUpUseCaseTests {

    @MockBean
    private lateinit var commandStudentPort: CommandStudentPort

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var commandUserPort: StudentCommandUserPort

    @MockBean
    private lateinit var querySchoolPort: StudentQuerySchoolPort

    @MockBean
    private lateinit var queryAuthCodePort: StudentQueryAuthCodePort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var queryVerifiedStudentPort: StudentQueryVerifiedStudentPort

    @MockBean
    private lateinit var queryRoomPort: StudentQueryRoomPort

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var jwtPort: StudentJwtPort

    private lateinit var signUpUseCase: SignUpUseCase

    @BeforeEach
    fun setUp() {
        signUpUseCase = SignUpUseCase(
            commandStudentPort,
            queryStudentPort,
            commandUserPort,
            querySchoolPort,
            queryUserPort,
            queryAuthCodePort,
            queryVerifiedStudentPort,
            queryRoomPort,
            securityPort,
            jwtPort
        )
    }

    private val id = UUID.randomUUID()
    private val code = "12345678"
    private val email = "test@test.com"
    private val accountId = "test accountId"
    private val password = "test password"
    private val question = "test question"
    private val answer = "test answer"
    private val name = "test name"
    private val profileImageUrl = "test profileImage"

    private val schoolStub by lazy {
        School(
            id = id,
            name = "test name",
            code = code,
            question = question,
            answer = answer,
            address = "주소",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    private val authCodeStub by lazy {
        AuthCode(
            code = "123412",
            email = email,
            type = EmailType.SIGNUP
        )
    }

    private val verifiedStudentStub by lazy {
        VerifiedStudent(
            id = UUID.randomUUID(),
            schoolName = schoolStub.name,
            name = name,
            roomNumber = "318",
            roomLocation = "A",
            gcn = gcnStub,
            sex = Sex.FEMALE
        )
    }

    private val roomStub by lazy {
        Room(
            id = UUID.randomUUID(),
            number = verifiedStudentStub.roomNumber,
            schoolId = schoolStub.id
        )
    }

    private val requestStub by lazy {
        SignUpRequest(
            schoolCode = code,
            schoolAnswer = answer,
            email = email,
            authCode = "123412",
            grade = 1,
            classRoom = 1,
            number = 1,
            accountId = accountId,
            password = password,
            profileImageUrl = profileImageUrl
        )
    }

    private val gcnStub = Student.processGcn(
        grade = requestStub.grade,
        classRoom = requestStub.classRoom,
        number = requestStub.number
    )

//    @Test
//    fun `회원가입 성공`() {
//        // given
//        given(querySchoolPort.querySchoolByCode(code))
//            .willReturn(schoolStub)
//
//        given(queryUserPort.existsUserByEmail(email))
//            .willReturn(false)
//
//        given(queryAuthCodePort.queryAuthCodeByEmail(email))
//            .willReturn(authCodeStub)
//
//        given(queryUserPort.existsUserByAccountId(accountId))
//            .willReturn(false)
//
//        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
//            .willReturn(verifiedStudentStub)
//
//        given(queryRoomPort.queryRoomBySchoolIdAndNumber(schoolStub.id, verifiedStudentStub.roomNumber))
//            .willReturn(roomStub)
//
//        given(securityPort.encodePassword(requestStub.password))
//            .willReturn(password)
//
//        given(commandUserPort.saveUser(userStub))
//            .willReturn(savedUserStub)
//
//        given(commandStudentPort.saveStudent(studentStub))
//            .willReturn(savedStudentStub)
//
//        given(jwtPort.receiveToken(userStub.id, Authority.STUDENT))
//            .willReturn(tokenResponseStub)
//
//        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
//            .willReturn(featureStub)
//
//        // when
//        val response = signUpUseCase.execute(requestStub)
//
//        // then
//        assertThat(response).isEqualTo(signUpResponseStub)
//    }

    @Test
    fun `학교 인증코드에 해당하는 학교가 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(null)

        // when & then
        assertThrows<SchoolCodeMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학교 확인 질문에 대한 답변이 일치하지 않음`() {
        val wrongAnswerSchool = schoolStub.copy(answer = "wrong answer")

        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(wrongAnswerSchool)

        // when & then
        assertThrows<AnswerMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일이 이미 존재함`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(true)

        // when & then
        assertThrows<UserEmailExistsException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일 인증코드가 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일 인증코드가 일치하지 않음`() {
        val wrongCodeAuthCode = authCodeStub.copy(code = "wrong code")

        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(wrongCodeAuthCode)

        // when & then
        assertThrows<AuthCodeMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학번 이미 존재`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(
            queryStudentPort.existsStudentByGradeAndClassRoomAndNumber(
                requestStub.grade,
                requestStub.classRoom,
                requestStub.number
            )
        ).willReturn(true)

        // when & then
        assertThrows<StudentAlreadyExistsException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `검증된 학생 미존재`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(
            queryStudentPort.existsStudentByGradeAndClassRoomAndNumber(
                requestStub.grade,
                requestStub.classRoom,
                requestStub.number
            )
        ).willReturn(false)

        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
            .willReturn(null)

        // when & then
        assertThrows<VerifiedStudentNotFoundException> {
            signUpUseCase.execute(requestStub)
        }
    }

//    @Test
//    fun `호실 미존재`() {
//        // given
//        every { querySchoolPort.querySchoolByCode(code) } returns schoolStub
//
//        every { queryUserPort.existsUserByEmail(email) } returns false
//
//        every { queryAuthCodePort.queryAuthCodeByEmail(email) } returns authCodeStub
//
//        every {
//            queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(
//                gcnStub, schoolStub.name
//            )
//        } returns verifiedStudentStub
//
//        every { securityPort.encodePassword(requestStub.password) } returns password
//
//        every { commandUserPort.saveUser(any()) } returns userStub
//
//        every {
//            queryRoomPort.queryRoomBySchoolIdAndNumber(
//                schoolStub.id, verifiedStudentStub.roomNumber
//            )
//        } returns null
//
//        // when & then
//        assertThrows<RoomNotFoundException> {
//            signUpUseCase.execute(requestStub)
//        }
//    }

    @Test
    fun `아이디가 이미 존재함`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(
            queryStudentPort.existsStudentByGradeAndClassRoomAndNumber(
                requestStub.grade,
                requestStub.classRoom,
                requestStub.number
            )
        ).willReturn(false)

        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
            .willReturn(verifiedStudentStub)

        given(queryRoomPort.queryRoomBySchoolIdAndNumber(schoolStub.id, verifiedStudentStub.roomNumber))
            .willReturn(roomStub)

        given(queryUserPort.existsUserByAccountId(accountId))
            .willReturn(true)

        // when & then
        assertThrows<UserAccountIdExistsException> {
            signUpUseCase.execute(requestStub)
        }
    }

//    @Test
//    fun `이용 가능한 기능이 존재하지 않음`() {
//        //given
//        given(querySchoolPort.querySchoolByCode(code))
//            .willReturn(schoolStub)
//
//        given(queryUserPort.existsUserByEmail(email))
//            .willReturn(false)
//
//        given(queryAuthCodePort.queryAuthCodeByEmail(email))
//            .willReturn(authCodeStub)
//
//        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
//            .willReturn(verifiedStudentStub)
//
//        given(queryRoomPort.queryRoomBySchoolIdAndNumber(schoolStub.id, verifiedStudentStub.roomNumber))
//            .willReturn(roomStub)
//
//        given(queryUserPort.existsUserByAccountId(accountId))
//            .willReturn(false)
//
//        given(securityPort.encodePassword(requestStub.password))
//            .willReturn(userStub.password)
//
//        given(commandUserPort.saveUser(userStub))
//            .willReturn(savedUserStub)
//
//        given(commandStudentPort.saveStudent(studentStub))
//            .willReturn(savedStudentStub)
//
//        given(jwtPort.receiveToken(id, Authority.STUDENT))
//            .willReturn(tokenResponseStub)
//
//        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
//            .willReturn(null)
//
//        // when & then
//        assertThrows<FeatureNotFoundException> {
//            signUpUseCase.execute(requestStub)
//        }
//    }
}
