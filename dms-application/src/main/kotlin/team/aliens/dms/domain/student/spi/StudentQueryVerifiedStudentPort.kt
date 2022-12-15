package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.student.model.VerifiedStudent

interface StudentQueryVerifiedStudentPort {

    fun queryVerifiedStudentByGcnAndSchoolName(gcn: String, schoolName: String): VerifiedStudent?

}