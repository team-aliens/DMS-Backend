package team.aliens.dms.domain.notice.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse.NoticeDetails
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.spi.NoticeQueryUserPort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryAllNoticesUseCaseTests {

    @MockBean
    private lateinit var securityPort: NoticeSecurityPort

    @MockBean
    private lateinit var queryUserPort: NoticeQueryUserPort

    @MockBean
    private lateinit var  queryNoticePort: QueryNoticePort

    private lateinit var queryAllNoticesUseCase: QueryAllNoticesUseCase

    @BeforeEach
    fun setUp() {
        queryAllNoticesUseCase = QueryAllNoticesUseCase(securityPort, queryUserPort, queryNoticePort)
    }

    private val currentUserId = UUID.randomUUID()
    private val order = "NEW"
    private val schoolId = UUID.randomUUID()
    private val noticeId = UUID.randomUUID()
    private val createdAt = LocalDateTime.now()

    private val userStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            accountId = "아이디",
            password = "비밀번호",
            email = "이메일@dsm.hs.kr",
            name = "이름",
            profileImageUrl = "https://~",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val noticeStub by lazy {
        Notice(
            id = noticeId,
            managerId = UUID.randomUUID(),
            title = "제목",
            content = "내용",
            createdAt = createdAt,
            updatedAt = null
        )
    }

    private val queryAllNoticesResponseStub by lazy {
        QueryAllNoticesResponse(
            listOf(
                NoticeDetails(
                    id = noticeId,
                    title = "제목",
                    createdAt = createdAt
                )
            )
        )
    }

    @Test
    fun `공지사항 목록 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryNoticePort.queryAllNoticesBySchoolIdOrder(schoolId, OrderType.NEW))
            .willReturn(listOf(noticeStub))

        // when
        val response = queryAllNoticesUseCase.execute(order)

        // then
        assertEquals(response, queryAllNoticesResponseStub)
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
            queryAllNoticesUseCase.execute(order)
        }
    }
}