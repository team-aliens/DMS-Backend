package team.aliens.dms.domain.file.spi

import java.io.File
import team.aliens.dms.domain.student.model.VerifiedStudent

interface ParseFilePort {

    fun transferToVerifiedStudent(file: File): List<VerifiedStudent>

}