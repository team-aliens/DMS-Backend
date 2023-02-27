package team.aliens.dms.domain.point.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.PointHistoryDto
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryPointHistoryUseCaseTests {

    @MockBean
    private lateinit var securityPort: PointSecurityPort

    @MockBean
    private lateinit var queryStudentPort: PointQueryStudentPort

    @MockBean
    private lateinit var queryPointHistoryPort: QueryPointHistoryPort

    private lateinit var queryPointHistoryUseCase: QueryPointHistoryUseCase

    @BeforeEach
    fun setUp() {
        queryPointHistoryUseCase = QueryPointHistoryUseCase(
            securityPort, queryStudentPort, queryPointHistoryPort
        )
    }

    private val currentStudentId = UUID.randomUUID()

    private val studentStub by lazy {
        Student(
            id = currentStudentId,
            roomId = UUID.randomUUID(),
            roomNumber = "123",
            schoolId = UUID.randomUUID(),
            grade = 2,
            classRoom = 1,
            number = 6,
            name = "이름",
            profileImageUrl = "https://~",
            sex = Sex.FEMALE
        )
    }

    private val pageStub by lazy {
        PageData(
            page = 0,
            size = 10
        )
    }

    private val gcn = studentStub.gcn
    private val name = studentStub.name

    @Test
    fun `상벌점 내역 조회 성공(BONUS)`() {
        // given
        val pointStubs = listOf(
            PointHistoryDto(
                id = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.BONUS,
                name = "test name",
                score = 10
            ),
            PointHistoryDto(
                id = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.BONUS,
                name = "test name2",
                score = 5
            )
        )

        given(securityPort.getCurrentUserId())
            .willReturn(currentStudentId)

        given(queryStudentPort.queryStudentById(currentStudentId))
            .willReturn(studentStub)

        given(
            queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
                gcn = gcn,
                studentName = name,
                type = PointType.BONUS,
                isCancel = false,
                pageData = pageStub
            )
        ).willReturn(pointStubs)

        given(queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, name))
            .willReturn(Pair(15, 0))

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.BONUS, pageStub)

        println(response)

        // then
        assertAll(
            { assert(response.totalPoint == 15) },
            { assertThat(response.points).isNotEmpty }
        )
    }

    @Test
    fun `상벌점 내역 조회 성공(MINUS)`() {
        // given
        val pointStubs = listOf(
            PointHistoryDto(
                id = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.MINUS,
                name = "test name",
                score = 5
            ),
            PointHistoryDto(
                id = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.MINUS,
                name = "test name2",
                score = 5
            )
        )

        given(securityPort.getCurrentUserId())
            .willReturn(currentStudentId)

        given(queryStudentPort.queryStudentById(currentStudentId))
            .willReturn(studentStub)

        given(
            queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
                gcn = gcn,
                studentName = name,
                type = PointType.MINUS,
                isCancel = false,
                pageData = pageStub
            )
        ).willReturn(pointStubs)

        given(queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, name))
            .willReturn(Pair(15, 10))

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.MINUS, pageStub)

        // then
        assertAll(
            { assert(response.totalPoint == 10) },
            { assertThat(response.points).isNotEmpty }
        )
    }

    @Test
    fun `상벌점 내역 조회 성공(ALL)`() {
        // given
        val pointStubs = listOf(
            PointHistoryDto(
                id = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.BONUS,
                name = "test name",
                score = 10
            ),
            PointHistoryDto(
                id = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.MINUS,
                name = "test name2",
                score = 5
            )
        )

        given(securityPort.getCurrentUserId())
            .willReturn(currentStudentId)

        given(queryStudentPort.queryStudentById(currentStudentId))
            .willReturn(studentStub)

        given(
            queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
                gcn = gcn,
                studentName = name,
                type = null,
                isCancel = false,
                pageData = pageStub
            )
        ).willReturn(pointStubs)

        given(queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, name))
            .willReturn(Pair(10, 5))

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.ALL, pageStub)

        // then
        assertAll(
            { assert(response.totalPoint == 5) },
            { assertThat(response.points).isNotEmpty }
        )
    }

    @Test
    fun `학생이 존재하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentStudentId)

        given(queryStudentPort.queryStudentById(currentStudentId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            queryPointHistoryUseCase.execute(PointRequestType.ALL, pageStub)
        }
    }
}
