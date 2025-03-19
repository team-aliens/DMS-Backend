package team.aliens.dms.domain.vote.usecase

import org.springframework.transaction.annotation.Transactional
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.dto.request.CreateExcludedStudentRequest
import team.aliens.dms.domain.vote.model.ExcludedStudent
import team.aliens.dms.domain.vote.service.VoteService

@UseCase
class CreateExcludedStudentUseCase(
    val voteService: VoteService,
    val studentService: StudentService,
    val securityService: SecurityService
) {

    @Transactional
    fun execute(createExcludedStudentRequest: CreateExcludedStudentRequest) {
        val gcn = Student.parseGcn(createExcludedStudentRequest.gcn)
        val schoolId = securityService.getCurrentSchoolId()

        val studentId = studentService.getStudentBySchoolIdAndGcn(
            schoolId,
            gcn.first,
            gcn.second,
            gcn.third
        ).userId

        voteService.saveExcludedStudent(
            ExcludedStudent(
                studentId!!,
                schoolId
            )
        )
    }
}
