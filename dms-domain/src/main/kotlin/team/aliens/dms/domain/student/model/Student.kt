package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class Student(

    val id: UUID,

    val roomId: UUID,

    val roomNumber: String,

    val roomLocation: String,

    val schoolId: UUID,

    val grade: Int,

    val classRoom: Int,

    val number: Int,

    val name: String,

    val profileImageUrl: String? = PROFILE_IMAGE,

    val sex: Sex,

    val deletedAt: LocalDateTime? = null
) {

    val gcn: String = processGcn(this.grade, this.classRoom, this.number)

    companion object {
        const val PROFILE_IMAGE = "https://image-dms.s3.ap-northeast-2.amazonaws.com/59fd0067-93ef-4bcb-8722-5bc8786c5156%7C%7C%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3.png"

        fun processGcn(grade: Int, classRoom: Int, number: Int) = "${grade}${classRoom}${processNumber(number)}"

        private fun processNumber(number: Int) = if (number < 10) "0$number" else number.toString()
    }
}
