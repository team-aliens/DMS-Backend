package team.aliens.dms.domain.manager.spi

interface ManagerQueryPointPort {

    fun queryBonusAndMinusTotalPointByStudentGcnAndName(gcn: String, studentName: String): Pair<Int, Int>

}