package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.WarningTag
import team.aliens.dms.domain.tag.service.TagService
import java.time.LocalDateTime
import java.util.UUID

@SchedulerUseCase
class UpdateStudentTagsUseCase(
    private val pointService: PointService,
    private val tagService: TagService,
) {
    fun execute() {
        val warningTagMap: Map<String, UUID> = tagService.getAllWarningTags(
            WarningTag.getAllNames()
        ).associate { it.name to it.id }

        tagService.deleteAllStudentTagsByTagIdIn(warningTagMap.values.toList())

        val saveList: List<StudentTag> = pointService.getPointTotalsGroupByStudent().mapNotNull {
            val warningTag = WarningTag.byPoint(it.minusTotal)
            if (warningTag != WarningTag.SAFE) {
                StudentTag(
                    it.studentId,
                    warningTagMap[warningTag.name]!!,
                    LocalDateTime.now()
                )
            } else null
        }

        tagService.saveAllStudentTags(saveList)
    }
}
