package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.service.RemainService
import java.util.UUID

@UseCase
class RemoveRemainOptionUseCase(
    private val remainService: RemainService
) {

    fun execute(remainOptionId: UUID) {

        val remainOption = remainService.getRemainOptionById(remainOptionId)

        remainService.deleteRemainOption(remainOption)
    }
}
