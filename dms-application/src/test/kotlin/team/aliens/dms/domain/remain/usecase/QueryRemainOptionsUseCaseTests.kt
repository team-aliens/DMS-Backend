package team.aliens.dms.domain.remain.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.remain.stub.createRemainOptionStub
import team.aliens.dms.domain.remain.stub.createRemainStatusStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryRemainOptionsUseCaseTests {

    @MockBean
    private lateinit var securityPort: RemainSecurityPort

    @MockBean
    private lateinit var queryUserPort: RemainQueryUserPort

    @MockBean
    private lateinit var queryRemainOptionPort: QueryRemainOptionPort

    @MockBean
    private lateinit var queryRemainStatusPort: QueryRemainStatusPort

    private lateinit var queryRemainOptionsUseCase: QueryRemainOptionsUseCase

    @BeforeEach
    fun setUp() {
        queryRemainOptionsUseCase = QueryRemainOptionsUseCase(
            securityPort,
            queryUserPort,
            queryRemainOptionPort,
            queryRemainStatusPort
        )
    }

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val remainOptionId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId,
            schoolId = schoolId
        )
    }

    private val remainOptionStub by lazy {
        createRemainOptionStub(
            id = remainOptionId,
            schoolId = schoolId
        )
    }

    private val remainStatusStub by lazy {
        createRemainStatusStub(remainOptionId = remainOptionId)
    }

    @Test
    fun `잔류 신청 항목 목록 보기 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(queryRemainStatusPort.queryRemainStatusById(userId))
            .willReturn(remainStatusStub)

        given(queryRemainOptionPort.queryAllRemainOptionsBySchoolId(schoolId))
            .willReturn(listOf(remainOptionStub))

        // when & then
        assertDoesNotThrow {
            queryRemainOptionsUseCase.execute()
        }
    }

    @Test
    fun `유저를 찾지 못함`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            queryRemainOptionsUseCase.execute()
        }
    }
}
