package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.tag.dto.GrantTagRequest
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.time.LocalDateTime

@UseCase
class GrantTagUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryTagPort: QueryTagPort,
    private val queryStudentPort: QueryStudentPort,
    private val commandTagPort: CommandTagPort
) {

    fun execute(request: GrantTagRequest) {
        val currentManagerId = securityPort.getCurrentUserId()
        val currentManager = queryUserPort.queryUserById(currentManagerId) ?: throw UserNotFoundException
        val tag = queryTagPort.queryTagById(request.tagId) ?: throw TagNotFoundException

        validateSameSchool(currentManager.schoolId, tag.schoolId)

        val students = queryStudentPort.queryAllStudentsByIdsIn(request.studentIds)
        if (!students.map { it.id }.containsAll(request.studentIds)) {
            throw StudentNotFoundException
        }

        val studentTags = students.map {
            StudentTag(
                studentId = it.id,
                tagId = tag.id,
                createdAt = LocalDateTime.now()
            )
        }

        commandTagPort.saveAllStudentTags(studentTags)
    }
}
