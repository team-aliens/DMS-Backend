package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.model.WarningTag
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO
import java.time.LocalDateTime
import java.util.UUID

@SchedulerUseCase
class UpdateStudentTagsUseCase(
    private val schoolService: SchoolService,
    private val pointService: PointService,
    private val tagService: TagService
) {
    fun execute() {
        val school: School = schoolService.getAllSchools().first { it.name == "대덕소프트웨어마이스터고등학교" }

        val tagList: List<Tag> = tagService.getTagsBySchoolIdAndTagNameIn(school.id, WarningTag.getAllMessages())

        val warningTagMap: Map<String, UUID> = tagList.associate { it.name to it.id }

        val reverseWarningTagMap: Map<UUID, String> = tagList.associate { it.id to it.name }

        val studentTagDetailMap: Map<UUID, List<StudentTagDetailVO>> = tagService.getAllStudentTagDetails(school.id)
            .groupBy { it.studentId }

        val studentIdsToDeleteStudentTags = ArrayList<UUID>()

        val saveList: List<StudentTag> = pointService.getPointTotalsGroupByStudent(school.id).mapNotNull {
            val warningTag = WarningTag.byPoint(it.minusTotal)

            val isWarningTag = warningTag != WarningTag.SAFE
            val hasWarningTag = studentTagDetailMap.containsKey(it.studentId)
            val isHighLevelWarningAndCompleted = if (hasWarningTag) studentTagDetailMap[it.studentId]!!.any {
                WarningTag.byContent(it.tagName).point < warningTag.point && WarningTag.byContent(it.tagName).name.startsWith("C_")
            } else false

            if (isWarningTag &&
                (!hasWarningTag || isHighLevelWarningAndCompleted)
            ) {
                val ownedWarningTag = reverseWarningTagMap[
                    studentTagDetailMap[it.studentId]?.first()?.tagId
                ]?.let { WarningTag.byContent(it) } ?: WarningTag.SAFE

                studentIdsToDeleteStudentTags.add(it.studentId)

                StudentTag(
                    it.studentId,
                    warningTagMap[ownedWarningTag.nextLevel().warningMessage]!!,
                    LocalDateTime.now()
                )
            } else null
        }

        tagService.deleteAllStudentTagsByStudentIdIn(studentIdsToDeleteStudentTags)

        tagService.saveAllStudentTags(saveList)
    }
}
