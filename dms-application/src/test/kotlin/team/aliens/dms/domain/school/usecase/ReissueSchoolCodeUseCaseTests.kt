package team.aliens.dms.domain.school.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.CommandSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ReissueSchoolCodeUseCaseTests {

    @MockBean
    private lateinit var securityPort: SchoolSecurityPort

    @MockBean
    private lateinit var queryUserPort: SchoolQueryUserPort

    @MockBean
    private lateinit var querySchoolPort: QuerySchoolPort

    @MockBean
    private lateinit var commandSchoolPort: CommandSchoolPort

    private lateinit var reissueSchoolCodeUseCase: ReissueSchoolCodeUseCase

    @BeforeEach
    fun setUp() {
        reissueSchoolCodeUseCase = ReissueSchoolCodeUseCase(
            securityPort, queryUserPort, querySchoolPort, commandSchoolPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = currentUserId,
            schoolId = schoolId,
            accountId = "아이디",
            password = "비밀번호",
            email = "이메일",
            name = "이름",
            profileImageUrl = "https://~",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val schoolStub by lazy {
        School(
            id = schoolId,
            name = "이정윤",
            code = "12345678",
            question = "질문입니다",
            answer = "답변입니다",
            address = "주소입니다",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    @Test
    fun `인증코드 재발급 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(querySchoolPort.querySchoolById(userStub.schoolId))
            .willReturn(schoolStub)

        given(commandSchoolPort.saveSchool(schoolStub.copy(code = "09876543")))
            .willReturn(schoolStub)

        // when
        val response = schoolStub.copy(code = "09876543").code

        // then
        assertEquals(response, reissueSchoolCodeUseCase.execute())
    }

    @Test
    fun `유저 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            reissueSchoolCodeUseCase.execute()
        }
    }

    @Test
    fun `학교 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(querySchoolPort.querySchoolById(userStub.schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            reissueSchoolCodeUseCase.execute()
        }
    }
 }