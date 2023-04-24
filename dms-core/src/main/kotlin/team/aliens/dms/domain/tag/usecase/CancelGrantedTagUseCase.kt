package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class CancelGrantedTagUseCase(
    private val userService: UserService,
    private val tagService: TagService,
    private val studentService: StudentService,
) {

    fun execute(studentId: UUID, tagId: UUID) {

        val user = userService.getCurrentUser()
        val tag = tagService.getTagById(tagId, user.schoolId)

        val student = studentService.getStudentById(studentId, user.schoolId)

        tagService.deleteStudentTagById(
            studentId = student.id,
            tagId = tag.id
        )
    }
}
