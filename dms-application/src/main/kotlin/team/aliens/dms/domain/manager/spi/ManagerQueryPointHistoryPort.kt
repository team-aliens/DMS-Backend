package team.aliens.dms.domain.manager.spi

interface ManagerQueryPointHistoryPort {

    fun queryBonusAndMinusTotalPointByStudentGcnAndName(gcn: String, studentName: String): Pair<Int, Int>

}