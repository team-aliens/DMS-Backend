package team.aliens.dms.domain.student.service

import java.util.UUID
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.spi.QueryStudentPort

@Service
class CheckStudentServiceImpl(
    private val queryStudentPort: QueryStudentPort
) : CheckStudentService {
    override fun checkStudentExistsBySchoolIdAndGcnList(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>) {
        if (queryStudentPort.existsBySchoolIdAndGcnList(schoolId, gcnList)) {
            throw StudentAlreadyExistsException
        }
    }
}
