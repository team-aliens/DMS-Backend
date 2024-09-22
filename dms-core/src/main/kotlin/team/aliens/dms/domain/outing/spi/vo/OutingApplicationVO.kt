package team.aliens.dms.domain.outing.spi.vo

import team.aliens.dms.domain.student.model.Student
import java.time.LocalDate
import java.time.LocalTime

open class OutingApplicationVO(
    val studentName: String,
    val studentGrade: Int,
    val studentClassRoom: Int,
    val studentNumber: Int,
    val outingType: String,
    val outingDate: LocalDate,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val outingCompanionVOs: List<OutingCompanionVO>
) {
    val studentGcn = Student.processGcn(studentGrade, studentClassRoom, studentNumber)
}
