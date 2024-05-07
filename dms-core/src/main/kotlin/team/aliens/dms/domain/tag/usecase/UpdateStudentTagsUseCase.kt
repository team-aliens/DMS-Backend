package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.WarningTag
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO
import java.time.LocalDateTime
import java.util.UUID

@SchedulerUseCase
class UpdateStudentTagsUseCase(
    private val pointService: PointService,
    private val tagService: TagService,
) {
    fun execute() {
        val warningTagMap: Map<String, UUID> = tagService.getTagsByTagNameIn(
            WarningTag.getAllMessages()
        ).associate { it.name to it.id }

        val studentTagDetailMap: Map<UUID, List<StudentTagDetailVO>> = tagService.getAllStudentTagDetails()
            .groupBy { it.studentId }

        val deleteStudentIdList = ArrayList<UUID>()

        val saveList: List<StudentTag> = pointService.getPointTotalsGroupByStudent().mapNotNull {
            val warningTag = WarningTag.byPoint(it.minusTotal)

            val isWarningTag = warningTag != WarningTag.SAFE
            val hasNotWarningTag = !studentTagDetailMap.containsKey(it.studentId)
            val isHighLevelWarning = if (!hasNotWarningTag) studentTagDetailMap[it.studentId]!!.any {
                WarningTag.byContent(it.tagName).point < warningTag.point
            } else false

            if (isWarningTag &&
                (hasNotWarningTag || isHighLevelWarning)
            ) {

                deleteStudentIdList.add(it.studentId)
                StudentTag(
                    it.studentId,
                    warningTagMap[warningTag.warningMessage]!!,
                    LocalDateTime.now()
                )
            } else null
        }
        
        tagService.deleteAllStudentTagsByStudentIdIn(deleteStudentIdList)

        tagService.saveAllStudentTags(saveList)
    }
}
