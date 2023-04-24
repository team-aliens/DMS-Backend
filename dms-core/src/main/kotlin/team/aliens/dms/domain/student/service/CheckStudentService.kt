package team.aliens.dms.domain.student.service

import java.util.UUID

interface CheckStudentService {
    fun checkStudentExistsBySchoolIdAndGcnList(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>)
}
