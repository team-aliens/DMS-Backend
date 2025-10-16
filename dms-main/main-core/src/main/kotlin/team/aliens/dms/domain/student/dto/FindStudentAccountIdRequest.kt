package team.aliens.dms.domain.student.dto

data class FindStudentAccountIdRequest(
    val name: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
)
