package team.aliens.dms.domain.tag.model

import team.aliens.dms.domain.tag.exception.TagNotFoundException

enum class CompleteTag(val warningMessage: String, val point: Int) {
    C_FIRST_WARNING("경고 1단계(완료)", 15),
    C_SECOND_WARNING("경고 2단계(완료)", 20),
    C_THIRD_WARNING("경고 3단계(완료)", 25),
    C_ONE_OUT("OUT 1(완료)", 30),
    C_TWO_OUT("OUT 2(완료)", 35),
    C_THREE_OUT("OUT 3(완료)", 40);

    companion object {
        fun byContent(warningMessage: String): CompleteTag =
            when (warningMessage) {
                C_FIRST_WARNING.warningMessage -> C_FIRST_WARNING
                C_SECOND_WARNING.warningMessage -> C_SECOND_WARNING
                C_THIRD_WARNING.warningMessage -> C_THIRD_WARNING
                C_ONE_OUT.warningMessage -> C_ONE_OUT
                C_TWO_OUT.warningMessage -> C_TWO_OUT
                C_THREE_OUT.warningMessage -> C_THREE_OUT
                else -> throw TagNotFoundException
            }

        fun getAllMessages(): List<String> = listOf(
            C_FIRST_WARNING.warningMessage,
            C_SECOND_WARNING.warningMessage,
            C_THIRD_WARNING.warningMessage,
            C_ONE_OUT.warningMessage,
            C_TWO_OUT.warningMessage,
            C_THREE_OUT.warningMessage
        )
    }
}
