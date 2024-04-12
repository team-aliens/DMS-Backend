package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.WarningTag
import team.aliens.dms.domain.tag.service.TagService
import java.time.LocalDateTime

@SchedulerUseCase
class UpdateStudentTagsUseCase(
    private val studentService: StudentService,
    private val tagService: TagService,
    private val pointService: PointService,
) {
    fun execute() {

        val saveList = ArrayList<StudentTag>()

        studentService.getAllStudentWithMinusPoint().forEach { (studentId, minusPoint) ->
            val warningTag = WarningTag.ofByPoint(minusPoint)

            if (warningTag != WarningTag.SAFE) {
                tagService.getStudentTagsByStudentId(studentId).stream().forEach { tag ->
                    WarningTag.ofByNameOrNull(tagService.getTagById(tag.tagId).name)?.run {
                        if (this.ordinal < warningTag.ordinal)
                            tagService.deleteStudentTagById(
                                studentId,
                                tag.tagId
                            )
                    }
                }

                saveList.add(
                    StudentTag(
                        studentId,
                        tagService.getTagByName(warningTag.getTagName()).id,
                        LocalDateTime.now()
                    )
                )
            }
        }

        tagService.saveAllStudentTags(saveList)
    }
}
