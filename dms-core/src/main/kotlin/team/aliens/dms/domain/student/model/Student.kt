package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class Student(

    val id: UUID = UUID(0, 0),

    val userId: UUID? = null,

    val roomId: UUID,

    val roomNumber: String,

    val roomLocation: String,

    override val schoolId: UUID,

    val grade: Int,

    val classRoom: Int,

    val number: Int,

    val name: String,

    val profileImageUrl: String = PROFILE_IMAGE,

    val sex: Sex,

    val deletedAt: LocalDateTime? = null
) : SchoolIdDomain {

    val gcn: String = processGcn(this.grade, this.classRoom, this.number)

    val hasUser: Boolean
        get() = userId != null

    companion object {
        private const val DECIMAL_NUMBER = 10
        const val PROFILE_IMAGE = "https://image-dms.s3.ap-northeast-2.amazonaws.com/" +
            "59fd0067-93ef-4bcb-8722-5bc8786c5156%7C%7C%E1%84%83%E1%85%A1%E1%84%8B" +
            "%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3.png"

        fun processGcn(grade: Int, classRoom: Int, number: Int) = "${grade}${classRoom}${processNumber(number)}"

        private fun processNumber(number: Int) = if (number < DECIMAL_NUMBER) "0$number" else number.toString()
    }
}
