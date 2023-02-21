package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.student.model.VerifiedStudent
import java.io.File

interface ParseFilePort {

    fun transferToVerifiedStudent(file: File): List<VerifiedStudent>

    fun writePointHistoryExcelFile(pointHistories: List<PointHistory>): ByteArray

}