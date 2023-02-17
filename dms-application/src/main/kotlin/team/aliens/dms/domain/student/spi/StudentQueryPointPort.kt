package team.aliens.dms.domain.student.spi

interface StudentQueryPointPort {

    fun queryBonusAndMinusTotalPointByStudentGcnAndName(gcn: String, studentName: String): Pair<Int, Int>

}