package team.aliens.dms.domain.school.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.CommandSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.school.stub.createSchoolStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
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
        createUserStub(
            id = currentUserId, schoolId = schoolId, authority = Authority.MANAGER
        )
    }

    private val schoolStub by lazy {
        createSchoolStub(id = schoolId)
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

        // when
        val response = reissueSchoolCodeUseCase.execute()

        // then
        assertThat(response).isNotBlank
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
