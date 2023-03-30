package team.aliens.dms.domain.manager.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.stub.createSchoolStub
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class FindManagerAccountIdUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: ManagerQuerySchoolPort

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var sendEmailPort: SendEmailPort

    private lateinit var findManagerAccountIdUseCase: FindManagerAccountIdUseCase

    @BeforeEach
    fun setUp() {
        findManagerAccountIdUseCase = FindManagerAccountIdUseCase(
            querySchoolPort, queryUserPort, sendEmailPort
        )
    }

    private val schoolId = UUID.randomUUID()

    private val schoolStub by lazy {
        createSchoolStub(
            id = schoolId,
            answer = "안희명"
        )
    }

    private val userStub by lazy {
        createUserStub(schoolId = schoolId)
    }

    @Test
    fun `아이디 찾기 성공`() {
        val answer = "안희명"

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(schoolStub)

        given(queryUserPort.queryUserBySchoolIdAndAuthority(schoolId, Authority.MANAGER))
            .willReturn(userStub)

        // when
        val response = findManagerAccountIdUseCase.execute(schoolId, answer)

        // then
        assertThat(response).isNotNull
    }

    @Test
    fun `학교를 찾을 수 없음`() {
        val answer = "끼"

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            findManagerAccountIdUseCase.execute(schoolId, answer)
        }
    }

    @Test
    fun `답변이 틀림`() {
        val answer = "김범진탈모새"

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(schoolStub)

        // when then
        assertThrows<AnswerMismatchException> {
            findManagerAccountIdUseCase.execute(schoolId, answer)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        val answer = "안희명"

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(schoolStub)

        given(queryUserPort.queryUserBySchoolIdAndAuthority(schoolId, Authority.MANAGER))
            .willReturn(null)

        // when & then
        assertThrows<ManagerNotFoundException> {
            findManagerAccountIdUseCase.execute(schoolId, answer)
        }
    }
}
