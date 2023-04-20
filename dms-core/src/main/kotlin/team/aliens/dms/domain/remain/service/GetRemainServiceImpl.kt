package team.aliens.dms.domain.remain.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse.RemainOptionElement
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.exception.RemainStatusNotFound
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.school.validateSameSchool
import java.util.UUID

@Service
class GetRemainServiceImpl(
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val queryRemainStatusPort: QueryRemainStatusPort
) : GetRemainService {

    override fun getRemainAvailableTimeBySchoolId(schoolId: UUID) =
        queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId)
            ?: throw RemainAvailableTimeNotFoundException

    override fun getRemainOptionById(remainOptionId: UUID, schoolId: UUID): RemainOption {
        return (queryRemainOptionPort.queryRemainOptionById(remainOptionId) ?: throw RemainOptionNotFoundException)
            .apply { validateSameSchool(this.schoolId, schoolId) }
    }

    override fun getAllRemainOptionsBySchoolId(schoolId: UUID, remainOptionId: UUID?): List<RemainOptionElement> {
        return queryRemainOptionPort.queryAllRemainOptionsBySchoolId(schoolId)
            .map {
                RemainOptionElement(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    isApplied = it.id == remainOptionId
                )
            }
    }

    override fun queryAllByStudentId(studentIds: List<UUID>) =
        queryRemainStatusPort.queryAllByStudentId(studentIds)

    override fun queryRemainStatusById(userId: UUID) =
        queryRemainStatusPort.queryRemainStatusById(userId)

    override fun getRemainStatusById(userId: UUID) =
        queryRemainStatusPort.queryRemainStatusById(userId) ?: throw RemainStatusNotFound
}
