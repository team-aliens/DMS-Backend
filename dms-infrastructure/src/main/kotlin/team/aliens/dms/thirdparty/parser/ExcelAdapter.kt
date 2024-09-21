package team.aliens.dms.thirdparty.parser

import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.file.spi.vo.ExcelStudentVO
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.thirdparty.parser.port.ExcelPort
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.format.DateTimeFormatter

@Component
class ExcelAdapter : ParseFilePort, WriteFilePort, ExcelPort() {

    override fun getExcelStudentVO(file: File): List<ExcelStudentVO> {
        val workbook = transferToExcel(file)

        val worksheet = workbook.getSheetAt(0)

        val excelStudentVOs = getExcelInfo(worksheet) { row ->
            row.run {
                ExcelStudentVO(
                    grade = getIntValue(0),
                    classRoom = getIntValue(1),
                    number = getIntValue(2),
                    sex = Sex.transferToSex(
                        getStringValue(3)
                    ),
                    name = getStringValue(4),
                    roomNumber = getIntValue(5).toString(),
                    roomLocation = getStringValue(6)
                )
            }
        }

        return excelStudentVOs.filterNotNull()
    }

    override fun writeStudentExcelFile(students: List<Student>): ByteArray {

        val attributes = listOf("학년", "반", "번호", "성별 (ex. 남, 여)", "이름", "호실번호 (ex. 301,  501)", "호실자리위치 (ex. A, B, C)")

        val studentsList: List<List<String>> = students.map {
            listOf(
                it.grade.toString(),
                it.classRoom.toString(),
                it.number.toString(),
                it.sex.korean,
                it.name,
                it.roomNumber,
                it.roomLocation
            )
        }

        return createExcelSheet(
            attributes = attributes,
            dataList = studentsList
        )
    }

    override fun writePointHistoryExcelFile(pointHistories: List<PointHistory>): ByteArray {

        val attributes = listOf("날짜", "학생 이름", "학번", "항목", "상/벌점", "부여 점수")

        val historyDataList: List<List<String>> = pointHistories.map {
            listOf(
                it.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                it.studentName,
                it.studentGcn,
                it.pointName,
                it.pointType.korean,
                it.pointScore.toString()
            )
        }

        return createExcelSheet(
            attributes = attributes,
            dataList = historyDataList
        )
    }

    override fun writePointHistoriesExcelFile(
        pointHistories: List<PointHistory>,
        students: List<StudentWithTag>
    ): ByteArray {

        val attributes = listOf("학번", "이름", "상점", "벌점", "상점 내역", "벌점 내역", "교육 단계")
        val pointHistoryMap = pointHistories.filter { !it.isCancel }.groupBy { it.studentGcn }

        val studentPointHistoryInfoList = students.map { student ->

            val recentPointHistory = pointHistoryMap[student.gcn]?.maxBy { it.createdAt }

            val (bonusPointHistory, minusPointHistory) = pointHistoryMap[student.gcn]?.let { pointHistories ->
                pointHistories.partition { it.pointType == PointType.BONUS }
            } ?: Pair(emptyList(), emptyList())

            val minusPointHistoryString = minusPointHistory.joinToString(separator = "") { pointStatus ->
                "[${pointStatus.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).format()}] ${pointStatus.pointName} (${pointStatus.pointScore}점)\n"
            }

            val bonusPointHistoryString = bonusPointHistory.joinToString(separator = "") { pointStatus ->
                "[${pointStatus.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}] ${pointStatus.pointName} (${pointStatus.pointScore}점)\n"
            }

            listOf(
                student.gcn,
                student.name,
                (recentPointHistory?.bonusTotal ?: 0).toString(),
                (recentPointHistory?.minusTotal ?: 0).toString(),
                bonusPointHistoryString.replace(Regex("[\\[\\]]"), ""),
                minusPointHistoryString.replace(Regex("[\\[\\]]"), ""),
                student.tags.joinToString { it.name }
            )
        }

        return createExcelSheet(
            attributes = attributes,
            dataList = studentPointHistoryInfoList,
        )
    }

    override fun writeRemainStatusExcelFile(studentRemainInfos: List<StudentRemainInfo>): ByteArray {
        val remainInfosList: List<List<String?>> = studentRemainInfos.map {
            listOf(
                it.studentGcn,
                it.studentName,
                it.optionName,
                null // 서명은 의미상 null
            )
        }

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet()

        var lastRowNum = 0
        for (grade in 1..3) {
            val attributes = listOf("학번", "이름", "신청 항목", "서명")
            val headerRow = sheet.createRow(lastRowNum)
            for (classNum in 1..4) {
                val classNumIdx = (classNum - 1) * 4 // 반 별로 출력
                insertDataAtRow(row = headerRow, data = attributes, style = getHeaderCellStyle(workbook), startIdx = classNumIdx)

                for (number in 1..18) {
                    val row = sheet.getRow(lastRowNum + number) ?: sheet.createRow(lastRowNum + number)

                    val studentGcn = "${grade}${classNum}${number.toString().padStart(2, '0')}"
                    val studentInfos = remainInfosList.find { it[0] == studentGcn } ?: emptyList()

                    insertDataAtRow(
                        row = row,
                        data = studentInfos,
                        style = getDefaultCellStyle(workbook),
                        startIdx = classNumIdx
                    )
                }
            }
            lastRowNum += 18
        }
        formatWorkSheet(sheet)

        ByteArrayOutputStream().use { stream ->
            workbook.write(stream)
            return stream.toByteArray()
        }
    }

    override fun addStudyRoomApplicationStatusExcelFile(
        baseFile: File,
        timeSlots: List<TimeSlot>,
        studentSeatsMap: Map<Pair<String, String>, StudentSeatInfo>,
    ): ByteArray {
        val workbook = transferToExcel(baseFile)
        val worksheet = workbook.getSheetAt(0)

        val columnCount = worksheet.getRow(0).lastCellNum.toInt()
        insertDataAtRow(
            row = worksheet.getRow(0),
            startIdx = columnCount,
            data = timeSlots.map { it.name },
            style = getHeaderCellStyle(workbook)
        )

        val gcns = getExcelInfo(worksheet) { row ->
            row.run {
                Pair(
                    Student.processGcn(getIntValue(0), getIntValue(1), getIntValue(2)),
                    getStringValue(3)
                )
            }
        }

        for (i in 1..worksheet.lastRowNum) {
            val row = worksheet.getRow(i)
            if (row.isFirstCellBlank()) continue

            val studentSeats = studentSeatsMap[gcns[i - 1]]?.seats
            insertDataAtRow(
                row = row,
                startIdx = columnCount,
                data = timeSlots.map { timeSlot ->
                    studentSeats?.singleOrNull { it.timeSlotId == timeSlot.id }?.seatFullName
                },
                style = getDefaultCellStyle(workbook)
            )
        }
        formatWorkSheet(worksheet)

        ByteArrayOutputStream().use { stream ->
            workbook.write(stream)
            return stream.toByteArray()
        }
    }

    override fun writeStudyRoomApplicationStatusExcelFile(
        timeSlots: List<TimeSlot>,
        studentSeats: List<StudentSeatInfo>,
    ): ByteArray {
        val attributes = listOf("학년", "반", "번호", "이름", *timeSlots.map { it.name }.toTypedArray())

        val seatInfosList = studentSeats.map { studentSeat ->
            listOf(
                studentSeat.studentGrade.toString(),
                studentSeat.studentClassRoom.toString(),
                studentSeat.studentNumber.toString(),
                studentSeat.studentName,
                *timeSlots.map { timeSlot ->
                    studentSeat.seats?.singleOrNull { it.timeSlotId == timeSlot.id }?.seatFullName
                }.toTypedArray()
            )
        }

        return createExcelSheet(
            attributes = attributes,
            dataList = seatInfosList
        )
    }

    override fun writeOutingApplicationExcelFile(outingApplicationExcelVos: List<OutingApplicationVO>): ByteArray {
        val attributes = mutableListOf("ㅤ학번ㅤ", "ㅤ이름ㅤ", "외출 사유", "외출 시간", "도착 시간", "외출 확인", "복귀 확인")

        val outingApplicationInfoSet = outingApplicationExcelVos.map { outingApplication ->
            val outingApplicationInfoList = mutableListOf(
                listOf(
                    outingApplication.studentGcn,
                    outingApplication.studentName,
                    outingApplication.reason,
                    outingApplication.outingTime.toString(),
                    outingApplication.arrivalTime.toString(),
                    null,
                    null
                )
            )

            for (outingCompanions in outingApplication.outingCompanionVOs)
                if (outingCompanions.studentGcn.isNotBlank())
                    outingApplicationInfoList.add(
                        listOf(
                            outingCompanions.studentGcn,
                            outingCompanions.studentName,
                            null,
                            null,
                            outingApplication.outingTime.toString(),
                            outingApplication.arrivalTime.toString(),
                            null,
                            null
                        )
                    )

            outingApplicationInfoList
        }

        return createGroupExcelSheet(
            attributes = attributes,
            dataListSet = outingApplicationInfoSet,
            colors = listOf(
                IndexedColors.WHITE,
                IndexedColors.LIGHT_GREEN
            )
        )
    }
}
