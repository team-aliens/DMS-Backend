package team.aliens.dms.domain.tag.model

enum class WarningTag(val warningMessage: String, val point: Int) {
    SAFE("SAFE", 0),
    FIRST_WARNING("경고 1단계", 15),
    SECOND_WARNING("경고 2단계", 20),
    THIRD_WARNING("경고 3단계", 25),
    ONE_OUT("OUT 1", 30),
    TWO_OUT("OUT 2", 35),
    THREE_OUT("OUT 3", 40);

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
}
