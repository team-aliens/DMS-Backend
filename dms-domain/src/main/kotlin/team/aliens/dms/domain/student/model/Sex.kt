package team.aliens.dms.domain.student.model

enum class Sex {
    MALE,
    FEMALE,
    ALL
    ;

    companion object {
        const val MALE_KOREAN = "남"
        const val FEMALE_KOREAN = "여"
    }
}