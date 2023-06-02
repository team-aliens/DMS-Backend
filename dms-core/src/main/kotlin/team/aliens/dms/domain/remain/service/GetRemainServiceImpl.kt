package team.aliens.dms.domain.remain.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.exception.RemainStatusNotFound
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import java.util.UUID

@Service
class GetRemainServiceImpl(
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val queryRemainStatusPort: QueryRemainStatusPort,
    private val securityPort: SecurityPort,
    private val queryStudentPort: QueryStudentPort
) : GetRemainService {

    override fun getRemainAvailableTimeBySchoolId(schoolId: UUID) =
        queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId)
            ?: throw RemainAvailableTimeNotFoundException

    override fun getRemainOptionById(remainOptionId: UUID) =
        queryRemainOptionPort.queryRemainOptionById(remainOptionId) ?: throw RemainOptionNotFoundException

    override fun getAllRemainOptionsBySchoolId(schoolId: UUID) =
        queryRemainOptionPort.queryAllRemainOptionsBySchoolId(schoolId)

    override fun getAllRemainStatusInfoByStudentId(studentIds: List<UUID>) =
        queryRemainStatusPort.queryAllByStudentId(studentIds)

    override fun getRemainStatusById(studentId: UUID) =
        queryRemainStatusPort.queryRemainStatusById(studentId)

    override fun getRemainStatusByUserId(userId: UUID): RemainStatus? {
        if (securityPort.isStudent()) {
            val student = queryStudentPort.queryStudentByUserId(userId) ?: throw StudentNotFoundException
            return getRemainStatusById(student.id)
        } else return null
    }

    override fun getAppliedRemainOptionByStudentId(studentId: UUID): RemainOption {
        val remainStatus = getRemainStatusById(studentId) ?: throw RemainStatusNotFound
        return getRemainOptionById(remainStatus.remainOptionId)
    }
}
