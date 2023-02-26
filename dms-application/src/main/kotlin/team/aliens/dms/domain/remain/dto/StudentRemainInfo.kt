package team.aliens.dms.domain.remain.dto

import team.aliens.dms.domain.student.model.Sex

data class StudentRemainInfo(
    val studentName: String,
    val studentGcn: String,
    val studentSex: Sex,
    val roomNumber: Int,
    val optionName: String?
)
