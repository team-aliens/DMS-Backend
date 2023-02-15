package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.student.model.Student

interface StudentQueryPointPort {

    fun queryBonusAndMinusTotalPointByStudent(student: Student): Pair<Int, Int>

}