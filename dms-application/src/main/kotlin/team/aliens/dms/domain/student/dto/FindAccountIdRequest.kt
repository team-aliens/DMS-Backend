package team.aliens.dms.domain.student.dto

data class FindAccountIdRequest(

    val name: String,

   val grade: Int,

    val classRoom: Int,

    val number: Int,
)
