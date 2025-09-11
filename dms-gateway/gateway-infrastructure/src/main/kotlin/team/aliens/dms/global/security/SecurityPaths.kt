package team.aliens.dms.global.security

object SecurityPaths {

    val PERMIT_ALL_PATHS = listOf(
        // healthcheck
        "/",

        // auth
        "/auth/account-id",
        "/auth/email",
        "/auth/code",
        "/auth/tokens",
        "/auth/reissue",
        "/auth/passport",

        // students
        "/students/email/duplication",
        "/students/account-id/duplication",
        "/students/account-id/{school-id}",
        "/students/name",
        "/students/signup",
        "/students/password/initialization",

        // managers
        "/managers/account-id/{school-id}",
        "/managers/password/initialization",

        // schools
        "/schools",
        "/schools/question/{school-id}",
        "/schools/answer/{school-id}",
        "/schools/code",

        // files
        "/files",
        "/files/url",

        // templates
        "/templates",
    )
}
