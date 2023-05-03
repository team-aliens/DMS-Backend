package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.service.RemainService
import java.util.UUID

@UseCase
class UpdateRemainOptionUseCase(
    private val remainService: RemainService
) {

    fun execute(remainOptionId: UUID, title: String, description: String) {

        val remainOption = remainService.getRemainOptionById(remainOptionId)

        remainService.saveRemainOption(
            remainOption.copy(
                title = title,
                description = description
            )
        )
    }
}
