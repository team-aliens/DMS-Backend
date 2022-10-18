package team.aliens.dms.domain.manager

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.exception.AnswerNotMatchedException
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.usecase.FindAccountIdUseCase
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.global.spi.CoveredEmailPort
import java.time.LocalDate
import java.util.UUID


@ExtendWith(SpringExtension::class)
class FindAccountIdUseCaseTest {

    @MockBean
    private lateinit var managerQuerySchoolPort: ManagerQuerySchoolPort

    @MockBean
    private lateinit var managerQueryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var managerSecurityPort: ManagerSecurityPort

    @MockBean
    private lateinit var coveredEmailPort: CoveredEmailPort

    private lateinit var findAccountIdUseCase: FindAccountIdUseCase

    @BeforeEach
    fun setUp() {
        findAccountIdUseCase = FindAccountIdUseCase(
            managerQuerySchoolPort,
            managerQueryUserPort,
            managerSecurityPort,
            coveredEmailPort
        )
    }

    private val id = UUID.randomUUID()

    private val school = School(
        id = id,
        name = "대덕소프트웨어마이스터고등학교",
        code = "&AND@P",
        question = "전 교장선생님 이름은?",
        answer = "안희명",
        address = "대전광역시 유성구 가정북로 76",
        contractStartedAt = LocalDate.now(),
        contractEndedAt = null
    )

    private val user = User(
        id = id,
        schoolId = id,
        accountId = "accountId",
        password = "password",
        email = "email@dsm.hs.kr",
        name = "김범지인",
        profileImageUrl = "https://~~",
        createdAt = null,
        deletedAt = null
    )

    @Test
    fun `아이디 찾기 성공`() {
        val answer = "안희명"
        val coveredEmail = "e****@dsm.hs.kr"

        //given
        given(managerQuerySchoolPort.querySchoolById(id))
            .willReturn(school)
        given(managerSecurityPort.getCurrentUserId())
            .willReturn(id)
        given(managerQueryUserPort.queryUserById(id))
            .willReturn(user)
        given(coveredEmailPort.coveredEmail(user.email))
            .willReturn(coveredEmail)

        //when
        val response = findAccountIdUseCase.execute(id, answer)

        //then
        assertThat(response).isNotNull()
    }

    @Test
    fun `아이디 찾기 실패`() {
        val answer = "김범진"
        val coveredEmail = "e****@dsm.hs.kr"

        //given
        given(managerQuerySchoolPort.querySchoolById(id))
            .willReturn(school)
        given(managerSecurityPort.getCurrentUserId())
            .willReturn(id)
        given(managerQueryUserPort.queryUserById(id))
            .willReturn(user)
        given(coveredEmailPort.coveredEmail(user.email))
            .willReturn(coveredEmail)

        //when then
        assertThrows<AnswerNotMatchedException> {
            findAccountIdUseCase.execute(id, answer)
        }
    }
}