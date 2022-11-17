package team.aliens.dms.domain.manager.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.manager.dto.ManagerMyPageResponse
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ManagerMyPageUseCaseTests {

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    @MockBean
    private lateinit var querySchoolPort: ManagerQuerySchoolPort

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    private lateinit var managerMyPageUseCase: ManagerMyPageUseCase

    @BeforeEach
    fun setUp() {
        managerMyPageUseCase = ManagerMyPageUseCase(securityPort, querySchoolPort, queryUserPort)
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
            Authority.MANAGER,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val schoolStub by lazy {
        School(
            id = schoolId,
            name = "학교이름",
            code = "12345678",
            question = "질문",
            answer = "답변",
            address = "주소",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    private val managerMyPageResponse by lazy {
        ManagerMyPageResponse(
            schoolId = schoolId,
            schoolName = "학교이름",
            code = "12345678",
            question = "질문",
            answer = "답변"
        )
    }

    @Test
    fun `관리자 마이페이지 확인 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(querySchoolPort.querySchoolById(userStub.schoolId))
            .willReturn(schoolStub)

        // when
        val response = managerMyPageUseCase.execute()

        // then
        assertEquals(response, managerMyPageResponse)
    }

    @Test
    fun `사용자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            managerMyPageUseCase.execute()
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
            managerMyPageUseCase.execute()
        }
    }
}