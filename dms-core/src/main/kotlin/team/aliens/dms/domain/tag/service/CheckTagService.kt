package team.aliens.dms.domain.tag.service

import java.util.UUID

interface CheckTagService {

    fun checkTagExistsByNameAndSchoolId(name: String, schoolId: UUID)
}
