package team.aliens.dms.domain.auth.model

enum class EmailType(
    val fileName: String,
    val templateName: String,
    val templateSubject: String
) {

    SIGNUP("signup_template", "DMS_SIGNUP_TEMPLATE", "DMS 회원가입 이메일 승인코드"),
    PASSWORD("password_template", "DMS_PASSWORD_TEMPLATE", "DMS 비밀번호 변경 이메일 승인코드")
}
