package team.aliens.dms.domain.tag.model

enum class WarningTag(val warningMessage: String) {
    SAFE("SAFE"),
    FIRST_WARNING("경고 1단계"),
    SECOND_WARNING("경고 2단계"),
    THIRD_WARNING("경고 3단계"),
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

        fun getAllMessages(): List<String> = listOf(
            FIRST_WARNING.warningMessage,
            SECOND_WARNING.warningMessage,
            THIRD_WARNING.warningMessage,
            ONE_OUT.warningMessage,
            TWO_OUT.warningMessage,
            THREE_OUT.warningMessage
        )
    }

    fun getPoint(): Int =
        if (this == SAFE) 0 else (this.ordinal + 2) * 5
}
