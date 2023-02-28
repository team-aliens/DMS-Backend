package team.aliens.dms.thirdparty.parser

import com.fasterxml.uuid.Generators
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
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
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.thirdparty.parser.exception.ExcelExtensionMismatchException
import team.aliens.dms.thirdparty.parser.exception.ExcelInvalidFileException
import team.aliens.dms.thirdparty.parser.exception.ExcelSexMismatchException
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.format.DateTimeFormatter

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
                        name = getCell(0, CREATE_NULL_AS_BLANK).stringCellValue,
                        gcn = getCell(1, CREATE_NULL_AS_BLANK).numericCellValue.toInt().toString(),
                        roomNumber = getCell(2, CREATE_NULL_AS_BLANK).stringCellValue,
                        sex = transferToSex(
                            getCell(3, CREATE_NULL_AS_BLANK).stringCellValue
                        )
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

    private fun transferToSex(sex: String) = when (sex) {
        Sex.MALE.korean -> Sex.MALE
        Sex.FEMALE.korean -> Sex.FEMALE
        else -> throw ExcelSexMismatchException
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

        ByteArrayOutputStream().use { stream ->
            workbook.write(stream)
            return stream.toByteArray()
        }
    }

    private fun insertDatasAtRow(
        headerRow: XSSFRow,
        attributes: List<String?>,
        style: XSSFCellStyle
    ) {
        attributes.forEachIndexed { j, text ->
            val cell = headerRow.createCell(j)
            cell.setCellValue(text)
            cell.cellStyle = style
        }
    }

    private fun getHeaderCellStyle(workbook: XSSFWorkbook): XSSFCellStyle =
        workbook.createCellStyle()
            .apply {
                fillBackgroundColor = IndexedColors.GREY_25_PERCENT.index
                alignment = HorizontalAlignment.LEFT.ordinal.toShort()
                verticalAlignment = VerticalAlignment.CENTER.ordinal.toShort()
            }

    private fun getDefaultCellStyle(workbook: XSSFWorkbook): XSSFCellStyle =
        workbook.createCellStyle()
            .apply {
                alignment = HorizontalAlignment.LEFT.ordinal.toShort()
                verticalAlignment = VerticalAlignment.CENTER.ordinal.toShort()
            }
}
