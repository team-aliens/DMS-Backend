package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Student(

    val id: UUID,

    val roomId: UUID,

    val roomNumber: Int,

    val schoolId: UUID,

    val grade: Int,

    val classRoom: Int,

    val number: Int,

    val name: String,

    val profileImageUrl: String? = PROFILE_IMAGE

) {

    val gcn: String = "${this.grade}${this.classRoom}${processNumber()}"

    private fun processNumber() = if (number < 10) "0${number}" else number.toString()

    companion object {
        const val PROFILE_IMAGE = "a" // TODO 기본 프로필 이미지 넣기
    }
}