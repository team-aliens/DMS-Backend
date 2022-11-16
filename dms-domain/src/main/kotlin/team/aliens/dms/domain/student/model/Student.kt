package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Student(

    val studentId: UUID,

    val roomId: UUID,

    val schoolId: UUID,

    val grade: Int,

    val classRoom: Int,

    val number: Int,

    val name: String,

    val profileImageUrl: String? = PROFILE_IMAGE

) {

    val gcn: String = "${this.grade}${this.classRoom}${processedNumber()}"

    private fun processedNumber() = if (number < 10) "0${number}".toInt() else number

    companion object {
        const val PROFILE_IMAGE = "a" // TODO 기본 프로필 이미지 넣기
    }
}