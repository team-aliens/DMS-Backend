package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class UpdateRemainOptionUseCase(
    private val userService: UserService,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val commendRemainOptionPort: CommandRemainOptionPort
) {

    fun execute(remainOptionId: UUID, title: String, description: String) {

        val user = userService.getCurrentUser()
        val remainOption = queryRemainOptionPort.queryRemainOptionById(remainOptionId)
            ?: throw RemainOptionNotFoundException

        validateSameSchool(remainOption.schoolId, user.schoolId)

        commendRemainOptionPort.saveRemainOption(
            remainOption.copy(
                title = title,
                description = description
            )
        )
    }
}
