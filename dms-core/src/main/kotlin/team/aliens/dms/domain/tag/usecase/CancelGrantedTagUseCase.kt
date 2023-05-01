package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.tag.service.TagService
import java.util.UUID

@UseCase
class CancelGrantedTagUseCase(
    private val tagService: TagService,
    private val studentService: StudentService,
) {

    fun execute(studentId: UUID, tagId: UUID) {

        val student = studentService.getStudentById(studentId)
        val tag = tagService.getTagById(tagId)

        tagService.deleteStudentTagById(
            studentId = student.id,
            tagId = tag.id
        )
    }
}
