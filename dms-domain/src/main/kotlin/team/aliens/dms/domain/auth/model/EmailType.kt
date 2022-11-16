package team.aliens.dms.domain.auth.model

enum class EmailType(
    val templateName: String,
    val templateSubject: String
) {

    SIGNUP("DMS_SIGNUP_TEMPLATE", "DMS 회원가입 이메일 승인코드"),
    PASSWORD("DMS_PASSWORD_TEMPLATE", "DMS 비밀번호 변경 이메일 승인코드")
}