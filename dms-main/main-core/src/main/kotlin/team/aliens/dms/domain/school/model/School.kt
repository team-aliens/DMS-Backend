package team.aliens.dms.domain.school.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class School(

    val id: UUID = UUID(0, 0),

    val name: String,

    val code: String,

    val question: String,

    val answer: String,

    val address: String,

    val contractStartedAt: LocalDate,

    val contractEndedAt: LocalDate?

) {
    companion object {
        const val SCHOOL_CODE_SIZE: Int = 8
    }

    fun checkAnswer(answer: String) {
        if (this.answer != answer) {
            throw AnswerMismatchException
        }
    }
}
