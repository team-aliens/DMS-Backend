package team.aliens.dms.persistence.student.repository.vo

import com.querydsl.core.annotations.QueryProjection
import java.util.*

data class QueryStudentWithPointVO @QueryProjection constructor(
    val name: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val bonusTotal: Int?,
    val minusTotal: Int?
)
