package team.aliens.dms.thirdparty.parser

import com.fasterxml.uuid.Generators
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.format.DateTimeFormatter
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component
import team.aliens.dms.domain.file.FileExtension.XLS
import team.aliens.dms.domain.file.FileExtension.XLSX
import team.aliens.dms.domain.file.exception.BadExcelFormatException
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.thirdparty.parser.exception.ExcelExtensionMismatchException
import team.aliens.dms.thirdparty.parser.exception.ExcelInvalidFileException

@Component
class ExcelAdapter : ParseFilePort, WriteFilePort {

    override fun transferToVerifiedStudent(file: File, schoolName: String): List<VerifiedStudent> {
        val workbook = transferToExcel(file)

        val verifiedStudents = mutableListOf<VerifiedStudent>()

        try {
            val worksheet = workbook.getSheetAt(0)
            for (i in 1..worksheet.lastRowNum) {
                val excelData = worksheet.getRow(i).run {
                    VerifiedStudent(
                        id = Generators.timeBasedGenerator().generate(),
                        schoolName = schoolName,
                        gcn = Student.processGcn(
                            grade = getIntValue(0),
                            classRoom = getIntValue(1),
                            number = getIntValue(2)
                        ),
                        sex = Sex.transferToSex(
                            getStringValue(3)
                        ),
                        name = getStringValue(4),
                        roomNumber = getIntValue(5).toString(),
                        roomLocation = getStringValue(6)
                    )
                }
                verifiedStudents.add(excelData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw BadExcelFormatException
        }

        return verifiedStudents
    }

    private fun Row.getStringValue(idx: Int) = getCell(idx, CREATE_NULL_AS_BLANK).stringCellValue

    private fun Row.getIntValue(idx: Int) = getCell(idx, CREATE_NULL_AS_BLANK).numericCellValue.toInt()

    private fun transferToExcel(file: File): Workbook {
        val inputStream = file.inputStream()

        return inputStream.use {
            runCatching {
                when (file.extension.lowercase()) {
                    XLS -> HSSFWorkbook(inputStream)
                    XLSX -> XSSFWorkbook(inputStream)
                    else -> throw ExcelExtensionMismatchException
                }
            }.also {
                file.delete()
            }.onFailure {
                throw ExcelInvalidFileException
            }.getOrThrow()
        }
    }

    override fun writePointHistoryExcelFile(pointHistories: List<PointHistory>): ByteArray {

        val attributes = listOf("날짜", "학생 이름", "학번", "항목", "상/벌점", "부여 점수")

        val historyDatasList: List<List<String>> = pointHistories.map {
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
            datasList = historyDatasList
        )
    }

    override fun writeRemainStatusExcelFile(studentRemainInfos: List<StudentRemainInfo>): ByteArray {

        val attributes = listOf("학생 이름", "학번", "성별", "호실", "신청 항목")

        val remainInfosList: List<List<String?>> = studentRemainInfos.map {
            listOf(
                it.studentName,
                it.studentGcn,
                it.studentSex.korean,
                it.roomNumber,
                it.optionName
            )
        }

        return createExcelSheet(
            attributes = attributes,
            datasList = remainInfosList
        )
    }

    override fun addStudyRoomApplicationStatusExcelFile(
        baseFile: File,
        timeSlots: List<TimeSlot>,
        studentSeatsMap: Map<Pair<String, String>, StudentSeatInfo>,
    ): ByteArray {
        val workbook = transferToExcel(baseFile)
        val worksheet = workbook.getSheetAt(0)

        val columnCount = worksheet.getRow(0).lastCellNum.toInt()
        insertDatasAtRow(
            row = worksheet.getRow(0),
            startIdx = columnCount,
            datas = timeSlots.map { it.name },
            style = getHeaderCellStyle(workbook)
        )

        for (i in 1..worksheet.lastRowNum) {
            val row = worksheet.getRow(i)
            val studentSeat = row.run {
                try {
                    studentSeatsMap[
                        Pair(
                            Student.processGcn(getIntValue(0), getIntValue(1), getIntValue(2)),
                            getStringValue(3)
                        )
                    ]
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw BadExcelFormatException
                }
            } ?: throw UserNotFoundException

            val timeSlotIdx = studentSeat.timeSlotId?.let {
                timeSlots.indexOfFirst { it.id == studentSeat.timeSlotId }
            }
            insertDatasAtRow(
                row = row,
                startIdx = columnCount,
                datas = (timeSlots.indices).map {
                    if (it == timeSlotIdx) studentSeat.seatFullName else null
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

    private fun formatWorkSheet(
        worksheet: Sheet,
    ) {
        val lastCellNum = worksheet.getRow(0).lastCellNum.toInt()
        worksheet.apply {
            // 정렬 필터 적용
            setAutoFilter(CellRangeAddress(0, 0, 0, lastCellNum - 1))
            createFreezePane(0, 1)
            // 데이터에 맞춰 폭 조정
            (0 until lastCellNum)
                .map { autoSizeColumn(it) }
        }
    }

    override fun writeStudyRoomApplicationStatusExcelFile(
        timeSlots: List<TimeSlot>,
        studentSeats: List<StudentSeatInfo>,
    ): ByteArray {
        val attributes = listOf("학년", "반", "번호", "이름", *timeSlots.map { it.name }.toTypedArray())

        val remainInfosList = studentSeats.map { it ->
            val timeSlotIdx = it.timeSlotId?.let { timeSlots.indexOfFirst { t -> t.id == it } }
            listOf(
                it.studentGrade.toString(),
                it.studentClassRoom.toString(),
                it.studentNumber.toString(),
                it.studentName,
                *(timeSlots.indices)
                    .map { i -> if (i == timeSlotIdx) it.seatFullName else null }
                    .toTypedArray(),
            )
        }

        return createExcelSheet(
            attributes = attributes,
            datasList = remainInfosList
        )
    }

    private fun createExcelSheet(
        attributes: List<String>,
        datasList: List<List<String?>>
    ): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet()

        val headerRow = sheet.createRow(0)
        insertDatasAtRow(headerRow, attributes, getHeaderCellStyle(workbook))

        datasList.forEachIndexed { idx, datas ->
            val row = sheet.createRow(idx + 1)
            insertDatasAtRow(row, datas, getDefaultCellStyle(workbook))
        }
        formatWorkSheet(sheet)

        ByteArrayOutputStream().use { stream ->
            workbook.write(stream)
            return stream.toByteArray()
        }
    }

    private fun insertDatasAtRow(
        row: Row,
        datas: List<String?>,
        style: CellStyle,
    ) {
        datas.forEachIndexed { i, data ->
            val cell = row.createCell(i)
            data?.toIntOrNull()?.toDouble()?.let {
                cell.setCellValue(it)
            } ?: cell.setCellValue(data)
            cell.cellStyle = style
        }
    }

    private fun insertDatasAtRow(
        row: Row,
        startIdx: Int,
        datas: List<String?>,
        style: CellStyle,
    ) {
        datas.forEachIndexed { i, data ->
            val cell = row.createCell(i + startIdx)
            data?.toIntOrNull()?.toDouble()?.let {
                cell.setCellValue(it)
            } ?: cell.setCellValue(data)
            cell.cellStyle = style
        }
    }

    private fun getHeaderCellStyle(workbook: Workbook): CellStyle {
        val borderStyle = CellStyle.BORDER_THIN
        val borderColor = IndexedColors.BLACK.index

        return workbook.createCellStyle()
            .apply {
                fillForegroundColor = IndexedColors.YELLOW.index
                fillPattern = CellStyle.SOLID_FOREGROUND
                alignment = HorizontalAlignment.LEFT.ordinal.toShort()
                verticalAlignment = VerticalAlignment.CENTER.ordinal.toShort()
                borderLeft = borderStyle
                borderTop = borderStyle
                borderRight = borderStyle
                borderBottom = borderStyle
                leftBorderColor = borderColor
                topBorderColor = borderColor
                rightBorderColor = borderColor
                bottomBorderColor = borderColor
            }
    }

    private fun getDefaultCellStyle(workbook: Workbook): CellStyle =
        workbook.createCellStyle()
            .apply {
                alignment = HorizontalAlignment.LEFT.ordinal.toShort()
                verticalAlignment = VerticalAlignment.CENTER.ordinal.toShort()
            }
}
