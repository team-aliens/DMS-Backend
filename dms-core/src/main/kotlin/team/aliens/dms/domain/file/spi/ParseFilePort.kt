package team.aliens.dms.domain.file.spi

import java.io.File
import team.aliens.dms.domain.file.spi.vo.ExcelStudentVO

interface ParseFilePort {
    fun getExcelStudentVO(file: File): List<ExcelStudentVO>
}
