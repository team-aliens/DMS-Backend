package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.service.GetUserService
import java.util.UUID

@UseCase
class UpdatePointOptionUseCase(
    private val getUserService: GetUserService,
    private val queryPointOptionPort: QueryPointOptionPort,
    private val commandPointOptionPort: CommandPointOptionPort
) {

    fun execute(pointOptionId: UUID, name: String, score: Int) {

        val user = getUserService.getCurrentUser()

        val pointOption = queryPointOptionPort.queryPointOptionById(pointOptionId) ?: throw PointOptionNotFoundException

        validateSameSchool(pointOption.schoolId, user.schoolId)

        commandPointOptionPort.savePointOption(
            pointOption.copy(
                name = name,
                score = score
            )
        )
    }
}
