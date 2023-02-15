package team.aliens.dms.domain.point.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointPort
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
    private lateinit var queryPointPort: QueryPointPort

    private lateinit var queryPointHistoryUseCase: QueryPointHistoryUseCase

    @BeforeEach
    fun setUp() {
        queryPointHistoryUseCase = QueryPointHistoryUseCase(
            securityPort, queryStudentPort, queryPointPort
        )
    }

    private val currentStudentId = UUID.randomUUID()

    private val studentStub by lazy {
        Student(
            id = currentStudentId,
            roomId = UUID.randomUUID(),
            roomNumber = 123,
            schoolId = UUID.randomUUID(),
            grade = 2,
            classRoom = 1,
            number = 6,
            name = "이름",
            profileImageUrl = "https://~",
            sex = Sex.FEMALE
        )
    }

    @Test
    fun `상벌점 내역 조회 성공(BONUS)`() {
        // given
        val pointStubs = listOf(
            QueryPointHistoryResponse.Point(
                pointId = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.BONUS,
                name = "test name",
                score = 10
            ),
            QueryPointHistoryResponse.Point(
                pointId = UUID.randomUUID(),
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

        given(queryPointPort.queryPointHistoryByStudentAndType(studentStub, PointType.BONUS))
            .willReturn(pointStubs)

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.BONUS)

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
            QueryPointHistoryResponse.Point(
                pointId = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.MINUS,
                name = "test name",
                score = 5
            ),
            QueryPointHistoryResponse.Point(
                pointId = UUID.randomUUID(),
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

        given(queryPointPort.queryPointHistoryByStudentAndType(studentStub, PointType.MINUS))
            .willReturn(pointStubs)

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.MINUS)

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
            QueryPointHistoryResponse.Point(
                pointId = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.BONUS,
                name = "test name",
                score = 10
            ),
            QueryPointHistoryResponse.Point(
                pointId = UUID.randomUUID(),
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

        given(queryPointPort.queryAllPointHistoryByStudent(studentStub))
            .willReturn(pointStubs)

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.ALL)

        // then
        assertAll(
            { assert(response.totalPoint == 5) },
            { assertThat(response.points).isNotEmpty }
        )
    }
}