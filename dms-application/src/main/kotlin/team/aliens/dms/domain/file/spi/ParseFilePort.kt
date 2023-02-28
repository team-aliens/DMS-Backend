package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.student.model.VerifiedStudent
import java.io.File

interface ParseFilePort {
    fun transferToVerifiedStudent(file: File, schoolName: String): List<VerifiedStudent>
}
