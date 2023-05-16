package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.tag.dto.GrantTagRequest
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.service.TagService
import java.time.LocalDateTime

@UseCase
class GrantTagUseCase(
    private val tagService: TagService,
    private val studentService: StudentService,
) {

    fun execute(request: GrantTagRequest) {

        val tag = tagService.getTagById(request.tagId)

        val students = studentService.getAllStudentsByIdsIn(request.studentIds)

        val studentTags = students.map {
            StudentTag(
                studentId = it.id,
                tagId = tag.id,
                createdAt = LocalDateTime.now()
            )
        }

        tagService.saveAllStudentTags(studentTags)
    }
}
