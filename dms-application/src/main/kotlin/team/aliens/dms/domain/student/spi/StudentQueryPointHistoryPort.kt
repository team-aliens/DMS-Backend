package team.aliens.dms.domain.student.spi

interface StudentQueryPointHistoryPort {

    fun queryBonusAndMinusTotalPointByStudentGcnAndName(gcn: String, studentName: String): Pair<Int, Int>

}