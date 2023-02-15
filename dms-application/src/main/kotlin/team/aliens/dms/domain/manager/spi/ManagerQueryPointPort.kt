package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.student.model.Student

interface ManagerQueryPointPort {

    fun queryBonusAndMinusTotalPointByStudent(student: Student): Pair<Int, Int>

}