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
        studentService.getAllStudentsByName("").forEach { student ->
            val warningTag = WarningTag.of(
                pointService.queryBonusAndMinusTotalPointByStudentGcnAndName(
                    student.gcn,
                    student.name
                ).second
            )
            if (warningTag != WarningTag.SAFE) {
                var flag = true

                tagService.getStudentTagsByStudentId(student.id).stream()
                    .map { tag -> WarningTag.of(tagService.getTagById(tag.tagId).name) }
                    .forEach { tag ->
                        tag?.run {
                            if (this.ordinal < warningTag.ordinal)
                                tagService.deleteStudentTagById(
                                    student.id,
                                    tagService.getTagByName(this.getTagName()).id
                                )
                            else
                                flag = false
                        }
                    }

                if (flag)
                    tagService.saveStudentTag(
                        StudentTag(
                            student.id,
                            tagService.getTagByName(warningTag.getTagName()).id,
                            LocalDateTime.now()
                        )
                    )
            }
        }
    }
}
