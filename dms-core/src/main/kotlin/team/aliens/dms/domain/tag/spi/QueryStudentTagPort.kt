package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO
import java.util.*

interface QueryStudentTagPort {

    fun queryAllStudentTagDetails(schoolId: UUID): List<StudentTagDetailVO>
}
