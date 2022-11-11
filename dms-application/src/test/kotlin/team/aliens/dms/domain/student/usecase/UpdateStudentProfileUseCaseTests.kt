package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class UpdateStudentProfileUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var commandUserPort: StudentCommandUserPort

    private lateinit var updateStudentProfileUseCase: UpdateStudentProfileUseCase

    @BeforeEach
    fun setUp() {
        updateStudentProfileUseCase = UpdateStudentProfileUseCase(
            securityPort, queryUserPort, commandUserPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val profileImageUrl = "https://~"

    private val userStub by lazy {
        User(
            schoolId = UUID.randomUUID(),
            accountId = "계정아이디",
            password = "비밀번호",
            email = "이메일",
            name = "이름",
            profileImageUrl = profileImageUrl,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val userProfileNullStub by lazy {
        User(
            schoolId = UUID.randomUUID(),
            accountId = "계정아이디",
            password = "비밀번호",
            email = "이메일",
            name = "이름",
            profileImageUrl = null,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    @Test
    fun `프로필 변경 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(commandUserPort.saveUser(userStub.copy(profileImageUrl = "바뀐 사진 url")))
            .willReturn(userStub)

        // when & then
        assertDoesNotThrow {
            updateStudentProfileUseCase.execute(profileImageUrl)
        }
    }

    @Test
    fun `프로필 기본이미지로 변경 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userProfileNullStub)

        given(commandUserPort.saveUser(userProfileNullStub.copy(profileImageUrl = User.PROFILE_IMAGE)))
            .willReturn(userProfileNullStub)

        // when & then
        assertDoesNotThrow {
            updateStudentProfileUseCase.execute(profileImageUrl)
        }
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
            updateStudentProfileUseCase.execute(profileImageUrl)
        }
    }
}