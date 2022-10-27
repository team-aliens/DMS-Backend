package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import java.util.UUID

@ReadOnlyUseCase
class GetStudentDetailsUseCase(
//    private val queryUserPort: ManagerQueryUserPort,
//    private val query
) {

    fun execute(studentId: UUID): GetStudentDetailsResponse {
//        val user = queryUserPort.queryUserById(studentId) ?: throw UserNotFoundException
//
//
//        GetStudentDetailsResponse(
//            name = user.name,
//            gcn = ,
//            profileImageUrl = ,
//            bonusPoint = ,
//            minusPoint = ,
//            roomNumber = ,
//            roomMates =
//        )
        TODO()
    }
}