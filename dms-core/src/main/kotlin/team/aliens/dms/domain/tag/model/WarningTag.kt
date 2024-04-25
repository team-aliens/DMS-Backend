package team.aliens.dms.domain.tag.model

enum class WarningTag(warningMessage: String) {
    SAFE(""),
    FIRST_WARNING("1차 경고"),
    SECOND_WARNING("2차 경고"),
    THIRD_WARNING("3차 경고"),
    ONE_OUT("OUT 1"),
    TWO_OUT("OUT 2"),
    THREE_OUT("OUT 3");

    companion object {
        fun byPoint(minusPoint: Int) = when {
            40 <= minusPoint -> THREE_OUT
            35 <= minusPoint -> TWO_OUT
            30 <= minusPoint -> ONE_OUT
            25 <= minusPoint -> THIRD_WARNING
            20 <= minusPoint -> SECOND_WARNING
            15 <= minusPoint -> FIRST_WARNING
            else -> SAFE
        }

        fun getAllNames(): List<String> = listOf(
            FIRST_WARNING.name,
            SECOND_WARNING.name,
            THIRD_WARNING.name,
            ONE_OUT.name,
            TWO_OUT.name,
            THREE_OUT.name
        )
    }
}
