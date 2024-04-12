package team.aliens.dms.domain.tag.model

enum class WarningTag {
    SAFE,
    FIRST_WARNING,
    SECOND_WARNING,
    THIRD_WARNING,
    ONE_OUT,
    TWO_OUT,
    THREE_OUT;

    companion object {
        fun ofByPoint(warningLevel: Int) = when ((warningLevel - 10) / 5) {
            1 -> FIRST_WARNING
            2 -> SECOND_WARNING
            3 -> THIRD_WARNING
            4 -> ONE_OUT
            5 -> TWO_OUT
            in 6..Int.MAX_VALUE -> THREE_OUT
            else -> SAFE
        }

        fun ofByNameOrNull(warningMessage: String) = when (warningMessage) {
            "1차 경고" -> FIRST_WARNING
            "2차 경고" -> SECOND_WARNING
            "3차 경고" -> THIRD_WARNING
            "OUT 1" -> ONE_OUT
            "OUT 2" -> TWO_OUT
            "OUT 3" -> THREE_OUT
            else -> null
        }
    }

    fun getTagName(): String {
        return when (this.ordinal) {
            1 -> "1차 경고"
            2 -> "2차 경고"
            3 -> "3차 경고"
            4 -> "OUT 1"
            5 -> "OUT 2"
            6 -> "OUT 3"
            else -> "SAFE"
        }
    }
}
