package team.aliens.dms.domain.student.model

import team.aliens.dms.domain.student.exception.SexMismatchException

enum class Sex(
    val korean: String
) {
    MALE("남"),
    FEMALE("여"),
    ALL("전체")
    ;

    companion object {
        fun transferToSex(sex: String) = when (sex) {
            MALE.korean -> MALE
            FEMALE.korean -> FEMALE
            else -> throw SexMismatchException
        }
    }
}
