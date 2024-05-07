package team.aliens.dms.domain.tag.model

import team.aliens.dms.domain.tag.exception.TagNotFoundException

enum class WarningTag(val warningMessage: String, val point: Int) {
    SAFE("SAFE", 0),

    FIRST_WARNING("경고 1단계", 15),
    SECOND_WARNING("경고 2단계", 20),
    THIRD_WARNING("경고 3단계", 25),
    ONE_OUT("OUT 1", 30),
    TWO_OUT("OUT 2", 35),
    THREE_OUT("OUT 3", 40),

    C_FIRST_WARNING("경고 1단계(완료)", 15),
    C_SECOND_WARNING("경고 2단계(완료)", 20),
    C_THIRD_WARNING("경고 3단계(완료)", 25),
    C_ONE_OUT("OUT 1(완료)", 30),
    C_TWO_OUT("OUT 2(완료)", 35),
    C_THREE_OUT("OUT 3(완료)", 40);

    companion object {
        fun byContent(warningMessage: String): WarningTag =
            when (warningMessage) {
                FIRST_WARNING.warningMessage -> FIRST_WARNING
                SECOND_WARNING.warningMessage -> SECOND_WARNING
                THIRD_WARNING.warningMessage -> THIRD_WARNING
                ONE_OUT.warningMessage -> ONE_OUT
                TWO_OUT.warningMessage -> TWO_OUT
                THREE_OUT.warningMessage -> THREE_OUT

                C_FIRST_WARNING.warningMessage -> C_FIRST_WARNING
                C_SECOND_WARNING.warningMessage -> C_SECOND_WARNING
                C_THIRD_WARNING.warningMessage -> C_THIRD_WARNING
                C_ONE_OUT.warningMessage -> C_ONE_OUT
                C_TWO_OUT.warningMessage -> C_TWO_OUT
                C_THREE_OUT.warningMessage -> C_THREE_OUT
                else -> throw TagNotFoundException
            }

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
            THREE_OUT.warningMessage,

            C_FIRST_WARNING.warningMessage,
            C_SECOND_WARNING.warningMessage,
            C_THIRD_WARNING.warningMessage,
            C_ONE_OUT.warningMessage,
            C_TWO_OUT.warningMessage,
            C_THREE_OUT.warningMessage
        )
    }
}
