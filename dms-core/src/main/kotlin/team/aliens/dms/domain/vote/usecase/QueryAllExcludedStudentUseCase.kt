package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.dto.response.ExcludedStudentResponse
import team.aliens.dms.domain.vote.dto.response.ExcludedStudentsResponses
import team.aliens.dms.domain.vote.service.VoteService

@UseCase
class QueryAllExcludedStudentUseCase(
    val voteService: VoteService,
    val studentService: StudentService
) {

    fun execute(): ExcludedStudentsResponses {
        val excludedStudents = voteService.getAllExcludedStudents()
            .map { excludedStudent ->
                excludedStudent.studentId
            }.toList()

        val students = studentService.getAllStudentsByIdsIn(excludedStudents)

        return ExcludedStudentsResponses(
            students.map {
                ExcludedStudentResponse.of(
                    id = it.id,
                    gcn = Student.processGcn(it.grade, it.classRoom, it.number),
                    name = it.name
                )
            }
        )
    }
}
