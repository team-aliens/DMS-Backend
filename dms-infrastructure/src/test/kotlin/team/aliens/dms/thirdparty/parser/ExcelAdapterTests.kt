package team.aliens.dms.thirdparty.parser

import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo

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

    private val studentRemainInfos by lazy {
        listOf(
            StudentRemainInfo(
                studentName = "",
                studentGcn = "1234",
                studentSex = Sex.FEMALE,
                roomNumber = "200",
                optionName = ""
            )
        )
    }

    @Test
    fun `속성과 요소의 수가 일치한다 (writeRemainStatusExcelFile)`() {

        // given
        val attributes = slot<List<String>>()
        val datasList = slot<List<List<String>>>()

        every { excelAdapter["createExcelSheet"](capture(attributes), capture(datasList)) } returns byteArrayOf()

        // when
        excelAdapter.writeRemainStatusExcelFile(studentRemainInfos)

        // then
        assertAll(
            { assertEquals(studentRemainInfos.size, datasList.captured.size) },
            { assertEquals(datasList.captured[0].size, attributes.captured.size) }
        )
    }

    private val timeSlotStub by lazy {
        TimeSlot(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }

    private val timeSlotsStub by lazy {
        listOf(
            timeSlotStub,
            timeSlotStub.copy(id = UUID.randomUUID()),
            timeSlotStub.copy(id = UUID.randomUUID())
        )
    }

    private val studentSeatInfosStub by lazy {
        listOf(
            StudentSeatInfo(
                studentId = UUID.randomUUID(),
                studentName = "",
                studentGrade = 1,
                studentClassRoom = 2,
                studentNumber = 3,
                seatFullName = "",
                timeSlotId = timeSlotStub.id
            )
        )
    }

    @Test
    fun `속성과 요소의 수가 일치한다 (writeStudyRoomApplicationStatusExcelFile)`() {

        // given
        val attributes = slot<List<String>>()
        val datasList = slot<List<List<String>>>()

        every { excelAdapter["createExcelSheet"](capture(attributes), capture(datasList)) } returns byteArrayOf()

        // when
        excelAdapter.writeStudyRoomApplicationStatusExcelFile(
            timeSlots = timeSlotsStub,
            studentSeats = studentSeatInfosStub
        )

        // then
        assertAll(
            { assertEquals(studentSeatInfosStub.size, datasList.captured.size) },
            { assertEquals(datasList.captured[0].size, attributes.captured.size) }
        )
    }
}
