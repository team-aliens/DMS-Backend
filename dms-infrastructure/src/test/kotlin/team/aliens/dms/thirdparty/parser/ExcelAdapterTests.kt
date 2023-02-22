package team.aliens.dms.thirdparty.parser

import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDateTime
import java.util.UUID

class ExcelAdapterTests {

    private val excelAdapter: ExcelAdapter = spyk(recordPrivateCalls = true)

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
                schoolId = UUID.randomUUID()
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
                createdAt = LocalDateTime.of(2023, 3, 10, 12, 0),
                schoolId = UUID.randomUUID()
            )
        )
    }

    @Test
    fun `속성과 요소의 수가 일치한다 (writePointHistoryExcelFile)`() {

        // given
        val attributes = slot<List<String>>()
        val datasList = slot<List<List<String>>>()

        every { excelAdapter["createExcelSheet"](capture(attributes), capture(datasList)) } returns byteArrayOf()

        // when
        excelAdapter.writePointHistoryExcelFile(histories)

        // then
        assertAll(
            { assertEquals(histories.size, datasList.captured.size) },
            { assertEquals(datasList.captured[0].size, attributes.captured.size) }
        )
    }
}