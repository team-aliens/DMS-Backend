package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.point.dto.ExportAllPointHistoryResponse
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointQuerySchoolPort
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ExportAllPointHistoryUseCaseTests {

    private val securityPort: PointSecurityPort = mockk(relaxed = true)
    private val queryUserPort: PointQueryUserPort = mockk(relaxed = true)
    private val queryPointHistoryPort: QueryPointHistoryPort = mockk(relaxed = true)
    private val querySchoolPort: PointQuerySchoolPort = mockk(relaxed = true)
    private val writeFilePort: WriteFilePort = mockk(relaxed = true)

    private val exportAllPointHistoryUseCase = ExportAllPointHistoryUseCase(
        securityPort, queryUserPort, queryPointHistoryPort, querySchoolPort, writeFilePort
    )

    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = managerId,
            schoolId = schoolId,
            accountId = "accountId",
            password = "password",
            email = "email",
            authority = Authority.MANAGER,
            createdAt = null,
            deletedAt = null
        )
    }

    private val schoolStub by lazy {
        School(
            id = schoolId,
            name = "대덕소프트웨어마이스터고등학교",
            code = "test code",
            question = "test question",
            answer = "test answer",
            address = "test address",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = LocalDate.now(),
        )
    }

    private val start = LocalDateTime.of(2023, 2, 20, 12, 0)
    private val end = LocalDateTime.of(2023, 3, 15, 12, 0)

    private val oldestHistoryCreatedAt = LocalDateTime.of(2023, 3, 1, 12, 0)
    private val histories by lazy {
        listOf(
            PointHistory(
                studentName = "김은빈",
                studentGcn = "2106",
                bonusTotal = 3,
                minusTotal = 0,
                isCancel = false,
                pointName = "분리수거",
                pointScore = 3,
                pointType = PointType.BONUS,
                createdAt = LocalDateTime.of(2023, 3, 5, 12, 0),
                schoolId = schoolId
            ),
            PointHistory(
                studentName = "김은빈",
                studentGcn = "2106",
                bonusTotal = 3,
                minusTotal = 0,
                isCancel = false,
                pointName = "분리수거",
                pointScore = 3,
                pointType = PointType.BONUS,
                createdAt = oldestHistoryCreatedAt,
                schoolId = schoolId
            )
        )
    }

    private val fileStub = byteArrayOf()

    @Test
    fun `상벌점 내역 엑셀 출력 성공`() {
        // given
        val responseStub = ExportAllPointHistoryResponse(
            file = fileStub,
            fileName = "대덕소프트웨어마이스터고등학교_상벌점_부여내역_20230220_20230315"
        )

        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every {
            queryPointHistoryPort.queryPointHistoryBySchoolIdAndCreatedAtBetween(
                schoolId = schoolId,
                startAt = start,
                endAt = end
            )
        } returns histories

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every { querySchoolPort.querySchoolById(schoolId) } returns schoolStub

        every { writeFilePort.writePointHistoryExcelFile(histories) } returns fileStub

        // when
        val response = exportAllPointHistoryUseCase.execute(start, end)

        // then
        assertAll(
            { assertEquals(response.file, responseStub.file) },
            { assertEquals(response.fileName, responseStub.fileName) }
        )
    }

    @Test
    fun `start, end가 null인 경우`() {
        // given
        val responseStub = ExportAllPointHistoryResponse(
            file = fileStub,
            fileName = "대덕소프트웨어마이스터고등학교_상벌점_부여내역" +
                "_${oldestHistoryCreatedAt.format(File.FILE_DATE_FORMAT)}" +
                "_${LocalDate.now().format(File.FILE_DATE_FORMAT)}"
        )

        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every {
            queryPointHistoryPort.queryPointHistoryBySchoolIdAndCreatedAtBetween(
                schoolId = schoolId,
                startAt = null,
                endAt = any()
            )
        } returns histories

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every { querySchoolPort.querySchoolById(schoolId) } returns schoolStub

        every { writeFilePort.writePointHistoryExcelFile(histories) } returns fileStub

        // when
        val response = exportAllPointHistoryUseCase.execute(null, null)

        // then
        assertAll(
            { assertEquals(response.file, responseStub.file) },
            { assertEquals(response.fileName, responseStub.fileName) }
        )
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            exportAllPointHistoryUseCase.execute(start, end)
        }
    }
}
