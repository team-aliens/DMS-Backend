package team.aliens.dms.domain.remain.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.service.RemainService

@UseCase
class RemoveRemainOptionUseCase(
    private val remainService: RemainService
) {

    fun execute(remainOptionId: UUID) {

        val remainOption = remainService.getRemainOptionById(remainOptionId)

        remainService.deleteRemainOption(remainOption)
    }
}
