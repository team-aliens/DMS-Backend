package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.tag.model.CompleteTag
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
        val warningTagMap: Map<String, UUID> = tagService.getTagsByTagNameIn(
            WarningTag.getAllMessages()
        ).associate { it.name to it.id }

        val completeTagMap: Map<UUID, CompleteTag> = tagService.getTagsByTagNameIn(
            CompleteTag.getAllMessages()
        ).associate { it.id to CompleteTag.byContent(it.name) }

        val completeStudentTags: List<StudentTag> = tagService.getStudentTagsByTagNameIn(CompleteTag.getAllMessages())

        val deleteStudentIdList = ArrayList<UUID>()

        val saveList: List<StudentTag> = pointService.getPointTotalsGroupByStudent().mapNotNull {
            val warningTag = WarningTag.byPoint(it.minusTotal)
            if (warningTag != WarningTag.SAFE) {
                val tagId: UUID = if (completeStudentTags.all { studentTag ->
                        it.studentId != studentTag.studentId || warningTag.point > completeTagMap[studentTag.tagId]!!.point
                    }
                ) {
                    deleteStudentIdList.add(it.studentId)
                    warningTagMap[warningTag.warningMessage]!!
                } else {
                    completeTagMap.keys.filter { uuid -> completeStudentTags.any { studentTag -> studentTag.tagId == uuid && studentTag.studentId == it.studentId } }[0]
                }
                StudentTag(
                    it.studentId,
                    tagId,
                    LocalDateTime.now()
                )
            } else null
        }

        tagService.deleteAllStudentTagsByTagIdInOrStudentIdIn(warningTagMap.values.toList(), deleteStudentIdList)

        println(saveList)

        tagService.saveAllStudentTags(saveList)
    }
}
