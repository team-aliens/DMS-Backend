package team.aliens.dms.domain.point.service

import java.util.UUID

interface CheckPointService {
    fun checkPointOptionExistsByNameAndSchoolId(name: String, schoolId: UUID)
}
