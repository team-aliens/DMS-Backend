package team.aliens.dms.domain.manager.dto.request

object PasswordRegex {
    const val PATTERN = "(?=.*[a-z])(?=.*[0-9])(?=.*[!#\$%&'()*+,./:;<=>?@＼^_`{|}~-])[a-zA-Z0-9!#\$%&'()*+,./:;<=>?@＼^_`{|}~-]{8,20}\$"
    const val MESSAGE = "영문, 숫자, 기호를 포함한 8~20자여야 합니다."
}
