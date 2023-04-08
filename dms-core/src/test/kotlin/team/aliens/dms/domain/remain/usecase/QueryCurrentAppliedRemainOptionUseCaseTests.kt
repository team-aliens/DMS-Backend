package team.aliens.dms.domain.remain.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.remain.exception.RemainStatusNotFound
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.remain.stub.createRemainOptionStub
import team.aliens.dms.domain.remain.stub.createRemainStatusStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryCurrentAppliedRemainOptionUseCaseTests {

    @MockBean
    private lateinit var securityPort: RemainSecurityPort

    @MockBean
    private lateinit var queryRemainStatusPort: QueryRemainStatusPort

    @MockBean
    private lateinit var queryRemainOptionPort: QueryRemainOptionPort

    private lateinit var queryCurrentAppliedRemainOptionUseCase: QueryCurrentAppliedRemainOptionUseCase

    @BeforeEach
    fun setUp() {
        queryCurrentAppliedRemainOptionUseCase = QueryCurrentAppliedRemainOptionUseCase(
            securityPort,
            queryRemainStatusPort,
            queryRemainOptionPort
        )
    }

    private val userId = UUID.randomUUID()
    private val remainOptionId = UUID.randomUUID()

    private val remainOptionStub by lazy {
        createRemainOptionStub(id = remainOptionId)
    }

    private val remainStatusStub by lazy {
        createRemainStatusStub(remainOptionId = remainOptionId)
    }

    @Test
    fun `내 잔류 신청항목 보기 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryRemainStatusPort.queryRemainStatusById(userId))
            .willReturn(remainStatusStub)

        given(queryRemainOptionPort.queryRemainOptionById(remainOptionId))
            .willReturn(remainOptionStub)

        // when & then
        assertDoesNotThrow {
            queryCurrentAppliedRemainOptionUseCase.execute()
        }
    }

    @Test
    fun `잔류를 신청하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryRemainStatusPort.queryRemainStatusById(userId))
            .willReturn(null)

        // when & then
        assertThrows<RemainStatusNotFound> {
            queryCurrentAppliedRemainOptionUseCase.execute()
        }
    }
}
