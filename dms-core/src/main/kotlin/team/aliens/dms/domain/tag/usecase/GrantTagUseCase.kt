package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.tag.dto.GrantTagRequest
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.service.GetUserService
import java.time.LocalDateTime

@UseCase
class GrantTagUseCase(
    private val getUserService: GetUserService,
    private val queryTagPort: QueryTagPort,
    private val queryStudentPort: QueryStudentPort,
    private val commandTagPort: CommandTagPort
) {

    fun execute(request: GrantTagRequest) {

        val user = getUserService.getCurrentUser()
        val tag = queryTagPort.queryTagById(request.tagId) ?: throw TagNotFoundException

        validateSameSchool(user.schoolId, tag.schoolId)

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
