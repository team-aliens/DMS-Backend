package team.aliens.dms.thirdparty.parser

import com.fasterxml.uuid.Generators
import java.io.File
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component
import team.aliens.dms.domain.file.FileExtension.XLS
import team.aliens.dms.domain.file.FileExtension.XLSX
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.thirdparty.parser.exception.ExcelExtensionMismatchException
import team.aliens.dms.thirdparty.parser.exception.ExcelInvalidFileException
import team.aliens.dms.thirdparty.parser.exception.ExcelSexMismatchException

@Component
class ExcelAdapter : ParseFilePort {

    override fun transferToVerifiedStudent(file: File): List<VerifiedStudent> {
        val workbook = transferToExcel(file)

        val verifiedStudents = mutableListOf<VerifiedStudent>()

        try {
            val worksheet = workbook.getSheetAt(0)

            for (i in 1..worksheet.lastRowNum) {
                val excelData = worksheet.getRow(i).run {
                    VerifiedStudent(
                        id = Generators.timeBasedGenerator().generate(),
                        schoolName = getCell(0, CREATE_NULL_AS_BLANK).stringCellValue,
                        name = getCell(1, CREATE_NULL_AS_BLANK).stringCellValue,
                        gcn = getCell(2, CREATE_NULL_AS_BLANK).numericCellValue.toInt().toString(),
                        roomNumber = getCell(3, CREATE_NULL_AS_BLANK).numericCellValue.toInt(),
                        sex = transferToSex(
                            getCell(4, CREATE_NULL_AS_BLANK).stringCellValue
                        )
                    )
                }

                verifiedStudents.add(excelData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
        Sex.MALE_KOREAN -> Sex.MALE
        Sex.FEMALE_KOREAN -> Sex.FEMALE
        else -> throw ExcelSexMismatchException
    }
}