package team.aliens.dms.domain.point.service

import java.util.UUID

interface CheckPointService {
    fun checkPointOptionByNameAndSchoolId(name: String, schoolId: UUID)
}
