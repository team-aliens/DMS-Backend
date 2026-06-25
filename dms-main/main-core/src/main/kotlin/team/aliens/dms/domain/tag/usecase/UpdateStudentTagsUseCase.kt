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
    private val tagService: TagService
) {
    fun execute() {
        val warningTagEntities = tagService.getTagsByTagNameIn(WarningTag.getAllMessages())
        val warningTagByName: Map<String, UUID> = warningTagEntities.associate { it.name to it.id }
        val warningTagNames = warningTagByName.keys

        val studentWarningTagMap: Map<UUID, List<StudentTagDetailVO>> = tagService.getAllStudentTagDetails()
            .filter { it.tagName in warningTagNames }
            .groupBy { it.studentId }

        val warningTagsToDelete = mutableListOf<StudentTagDetailVO>()
        val tagsToSave = mutableListOf<StudentTag>()

        for (studentPoint in pointService.getPointTotalsGroupByStudent()) {
            val requiredLevel = WarningTag.byPoint(studentPoint.minusTotal)
            if (requiredLevel == WarningTag.SAFE) continue

            val currentWarningTags = studentWarningTagMap[studentPoint.studentId]

            if (currentWarningTags == null) {
                val tagId = warningTagByName[WarningTag.FIRST_WARNING.warningMessage] ?: continue
                tagsToSave.add(StudentTag(studentPoint.studentId, tagId, LocalDateTime.now()))
            } else {
                val completedLowerTag = currentWarningTags
                    .map { WarningTag.byContent(it.tagName) }
                    .filter { it.name.startsWith("C_") && it.point < requiredLevel.point }
                    .maxByOrNull { it.point }

                if (completedLowerTag != null) {
                    val nextLevel = completedLowerTag.nextLevel() ?: continue
                    val tagId = warningTagByName[nextLevel.warningMessage] ?: continue
                    warningTagsToDelete.addAll(currentWarningTags)
                    tagsToSave.add(StudentTag(studentPoint.studentId, tagId, LocalDateTime.now()))
                }
            }
        }

        warningTagsToDelete.forEach { tagService.deleteStudentTagById(it.studentId, it.tagId) }
        tagService.saveAllStudentTags(tagsToSave)
    }
}
