package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.domain.manager.dto.GetStudentListResponse
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.global.annotation.ReadOnlyUseCase

@ReadOnlyUseCase
class GetStudentListUseCase(
    private val queryUserPort: ManagerQueryUserPort,
//    private val queryStudentPort: ManageRQueryStudentPort
) {

    fun execute(name: String, sort: Sort) {

        val user = queryUserPort.searchStudent(name, sort)
//        val users = user.map {
//            queryStudentPort.queryStudentByUserId(it.id)

//        val students = user.map {
//            val student = queryStudentPort.queryByUserId(it.id)
//            GetStudentListResponse.StudentElement(
//                id = it.id,
//                name = it.name,
//                gcn =
//            )
//        }

        TODO("#71 merge")
    }
}