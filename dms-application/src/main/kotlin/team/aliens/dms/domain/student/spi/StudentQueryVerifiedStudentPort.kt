package team.aliens.dms.domain.student.spi

import java.util.UUID
import team.aliens.dms.domain.student.model.VerifiedStudent

interface StudentQueryVerifiedStudentPort {

    fun queryVerifiedStudentByGcnAndSchoolId(gcn: String, schoolId: UUID): VerifiedStudent?

}