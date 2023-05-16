package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.file.spi.vo.ExcelStudentVO
import java.io.File

interface ParseFilePort {
    fun getExcelStudentVO(file: File): List<ExcelStudentVO>
}
