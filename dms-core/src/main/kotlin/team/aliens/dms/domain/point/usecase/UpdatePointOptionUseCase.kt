package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@UseCase
class UpdatePointOptionUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryPointOptionPort: QueryPointOptionPort,
    private val commandPointOptionPort: CommandPointOptionPort
) {

    fun execute(pointOptionId: UUID, name: String, score: Int) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val pointOption = queryPointOptionPort.queryPointOptionById(pointOptionId) ?: throw PointOptionNotFoundException

        validateSameSchool(pointOption.schoolId, manager.schoolId)

        commandPointOptionPort.savePointOption(
            pointOption.copy(
                name = name,
                score = score
            )
        )
    }
}
