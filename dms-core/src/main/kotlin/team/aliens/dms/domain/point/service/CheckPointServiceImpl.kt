package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.point.exception.PointOptionNameExistsException
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import java.util.UUID

@Service
class CheckPointServiceImpl(
    private val queryPointOptionPort: QueryPointOptionPort
) : CheckPointService {

    override fun checkPointOptionExistsByNameAndSchoolId(name: String, schoolId: UUID) {
        if (queryPointOptionPort.existByNameAndSchoolId(name, schoolId)) {
            throw PointOptionNameExistsException
        }
    }
}
