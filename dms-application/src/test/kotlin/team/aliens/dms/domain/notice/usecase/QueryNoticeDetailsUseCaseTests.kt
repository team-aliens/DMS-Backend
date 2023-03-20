package team.aliens.dms.domain.notice.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.NoticeQueryUserPort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.notice.stub.createNoticeStub
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryNoticeDetailsUseCaseTests {

    @MockBean
    private lateinit var securityPort: NoticeSecurityPort

    @MockBean
    private lateinit var queryNoticePort: QueryNoticePort

    @MockBean
    private lateinit var queryUserPort: NoticeQueryUserPort

    private lateinit var queryNoticeDetailsUseCase: QueryNoticeDetailsUseCase

    @BeforeEach
    fun setUp() {
        queryNoticeDetailsUseCase = QueryNoticeDetailsUseCase(
            securityPort, queryNoticePort, queryUserPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val noticeId = UUID.randomUUID()
    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val createdAt = LocalDateTime.now()

//    private val noticeStub by lazy {
//        Notice(
//            id = UUID.randomUUID(),
//            managerId = UUID.randomUUID(),
//            title = "제목",
//            content = "내용",
//            createdAt = createdAt,
//            updatedAt = null
//        )
//    }

    private val noticeStub by lazy {
        createNoticeStub(createdAt = createdAt)
    }

    private val writerStub by lazy {
        createUserStub(
            id = managerId, schoolId = schoolId, authority = Authority.MANAGER
        )
    }

    private val viewerStub by lazy {
        createUserStub(id = currentUserId, schoolId = schoolId)
    }

    private val queryNoticeDetailsResponseStub by lazy {
        QueryNoticeDetailsResponse(
            title = "title",
            content = "content",
            createdAt = createdAt
        )
    }

    @Test
    fun `공지사항 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryNoticePort.queryNoticeById(noticeId))
            .willReturn(noticeStub)

        given(queryUserPort.queryUserById(noticeStub.managerId))
            .willReturn(writerStub)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(viewerStub)

        // when
        val response = queryNoticeDetailsUseCase.execute(noticeId)

        // then
        assertEquals(response, queryNoticeDetailsResponseStub)
    }

    @Test
    fun `공지사항 존재하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryNoticePort.queryNoticeById(noticeId))
            .willReturn(null)

        // when & then
        assertThrows<NoticeNotFoundException> {
            queryNoticeDetailsUseCase.execute(noticeId)
        }
    }

    @Test
    fun `작성자 존재하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryNoticePort.queryNoticeById(noticeId))
            .willReturn(noticeStub)

        given(queryUserPort.queryUserById(noticeStub.managerId))
            .willReturn(null)

        // when & then
        assertThrows<ManagerNotFoundException> {
            queryNoticeDetailsUseCase.execute(noticeId)
        }
    }

    @Test
    fun `관찰자 존재하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryNoticePort.queryNoticeById(noticeId))
            .willReturn(noticeStub)

        given(queryUserPort.queryUserById(noticeStub.managerId))
            .willReturn(writerStub)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            queryNoticeDetailsUseCase.execute(noticeId)
        }
    }

    @Test
    fun `학교 정보 불일치`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryNoticePort.queryNoticeById(noticeId))
            .willReturn(noticeStub)

        given(queryUserPort.queryUserById(noticeStub.managerId))
            .willReturn(writerStub)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(viewerStub.copy(schoolId = UUID.randomUUID()))

        // when & then
        assertThrows<SchoolMismatchException> {
            queryNoticeDetailsUseCase.execute(noticeId)
        }
    }
}
