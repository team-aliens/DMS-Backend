package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import team.aliens.dms.domain.file.service.FileService
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class ExportManagerDaybreakStudyApplicationUseCaseTest : DescribeSpec({
    val daybreakService = mockk<DaybreakService>()
    val fileService = mockk<FileService>()
    val securityService = mockk<SecurityService>()

    val useCase = ExportManagerDaybreakStudyApplicationUseCase(daybreakService, fileService, securityService)

    val schoolId = UUID.randomUUID()

    beforeTest {
        every { securityService.getCurrentSchoolId() } returns schoolId
    }

    describe("execute") {
        context("학년 필터 없이 새벽 자습 신청 엑셀을 export하면") {
            val applications = mockk<List<DaybreakStudyApplicationVO>>()
            val fileBytes = byteArrayOf(1, 2, 3)

            every { daybreakService.exportManagerDaybreakStudyApplications(schoolId, null) } returns applications
            every { fileService.writeDaybreakStudyApplicationExcelFile(applications) } returns fileBytes

            it("엑셀 파일과 파일명을 반환한다") {
                shouldNotThrowAny {
                    val response = useCase.execute(grade = null)

                    response shouldNotBe null
                    response.file shouldBe fileBytes
                    response.fileName shouldBe "새벽자습_신청현황_${LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
                }
            }
        }

        context("학년 필터를 지정하여 새벽 자습 신청 엑셀을 export하면") {
            val grade = 1
            val applications = mockk<List<DaybreakStudyApplicationVO>>()
            val fileBytes = byteArrayOf(4, 5, 6)

            every { daybreakService.exportManagerDaybreakStudyApplications(schoolId, grade) } returns applications
            every { fileService.writeDaybreakStudyApplicationExcelFile(applications) } returns fileBytes

            it("해당 학년의 엑셀 파일과 파일명을 반환한다") {
                shouldNotThrowAny {
                    val response = useCase.execute(grade = grade)

                    response shouldNotBe null
                    response.file shouldBe fileBytes
                }
            }
        }
    }
})
