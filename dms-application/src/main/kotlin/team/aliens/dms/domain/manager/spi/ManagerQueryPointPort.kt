package team.aliens.dms.domain.manager.spi

interface ManagerQueryPointPort {

    fun queryBonusAndMinusTotalPointByGcnAndStudentName(gcn: String, studentName: String): Pair<Int, Int>

}