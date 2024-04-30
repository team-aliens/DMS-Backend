package team.aliens.dms.domain.tag.model

import team.aliens.dms.domain.tag.exception.TagNotFoundException

enum class CompleteTag(val warningMessage: String) {
    C_FIRST_WARNING("경고 1단계(완료)"),
    C_SECOND_WARNING("경고 2단계(완료)"),
    C_THIRD_WARNING("경고 3단계(완료)"),
    C_ONE_OUT("OUT 1(완료)"),
    C_TWO_OUT("OUT 2(완료)"),
    C_THREE_OUT("OUT 3(완료)");

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

    fun getPoint(): Int =
        (this.ordinal + 3) * 5
}
