package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.usecase.UpdateStudentTagsUseCase

@Component
class StudentTagScheduler(
    private val updateStudentTagsUseCase: UpdateStudentTagsUseCase
) {

    /**
     * 매일 밤 12시 마다 학생이 가진 벌점 태그 초기화 후 벌점에 따른 태그 자동 부여
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun updateStudentTags() = updateStudentTagsUseCase.execute()
}
