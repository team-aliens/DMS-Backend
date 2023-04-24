package team.aliens.dms.domain.tag.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.tag.exception.TagAlreadyExistsException
import team.aliens.dms.domain.tag.spi.QueryTagPort
import java.util.UUID

@Service
class CheckTagServiceImpl(
    private val queryTagPort: QueryTagPort
) : CheckTagService {

    override fun checkTagExistsByNameAndSchoolId(name: String, schoolId: UUID) {
        if (queryTagPort.existsByNameAndSchoolId(name, schoolId)) {
            throw TagAlreadyExistsException
        }
    }
}
