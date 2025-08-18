package team.aliens.dms.domain.file.service

import team.aliens.dms.domain.file.spi.vo.ExcelStudentVO
import java.io.File

interface ParseFileService {
    fun getExcelStudentVO(file: File): List<ExcelStudentVO>
}
