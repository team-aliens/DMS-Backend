package team.aliens.dms.manager.dto.request

class Password(
    val value: String
) {
    companion object {
        const val PATTERN = "/^(?=.*[a-zA-Z])(?=.*[!@#\$%^&*+=-])(?=.*[0-9]).{8,20}\$/"
        const val MESSAGE = "영문, 숫자, 기호를 포함한 8~20자이어야 합니다."
    }
}