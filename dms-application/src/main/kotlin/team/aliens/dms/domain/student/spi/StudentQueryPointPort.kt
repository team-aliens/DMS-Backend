package team.aliens.dms.domain.student.spi

interface StudentQueryPointPort {

    fun queryBonusAndMinusTotalPointByGcnAndStudentName(gcn: String, studentName: String): Pair<Int, Int>

}