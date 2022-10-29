package team.aliens.dms.domain.notice.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
class QueryNoticeStatusUseCaseTests {

    @MockBean
    private lateinit var queryNoticePort: QueryNoticePort

    private lateinit var queryNoticeStatusUseCase: QueryNoticeStatusUseCase

    @BeforeEach
    fun setUp() {
        queryNoticeStatusUseCase = QueryNoticeStatusUseCase(queryNoticePort)
    }

    private val to = LocalDate.now()
    private val from = to.plusDays(7)

    @Test
    fun `7일 이내의 공지사항 존재함`() {
        // given
        given(queryNoticePort.existsNoticeByDateBetween(to, from))
            .willReturn(true)

        // when
        val response = queryNoticeStatusUseCase.execute()

        // then
        assertThat(response).isTrue
    }

    @Test
    fun `7일 이내의 공지사항 존재하지 않음`() {
        // given
        given(queryNoticePort.existsNoticeByDateBetween(to, from))
            .willReturn(false)

        // when
        val response = queryNoticeStatusUseCase.execute()

        // then
        assertThat(response).isFalse
    }
}