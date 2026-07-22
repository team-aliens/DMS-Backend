package team.aliens.dms.global.security

import org.springframework.http.HttpMethod

object SecurityPaths {

    data class PermitAllPath(
        val method: HttpMethod?,
        val path: String,
    )

    val PERMIT_ALL_PATHS = listOf(
        // healthcheck
        PermitAllPath(null, "/"),

        // auth
        PermitAllPath(HttpMethod.GET, "/auth/account-id"),
        PermitAllPath(HttpMethod.GET, "/auth/email"),
        PermitAllPath(HttpMethod.GET, "/auth/code"),
        PermitAllPath(HttpMethod.POST, "/auth/code"),
        PermitAllPath(HttpMethod.POST, "/auth/tokens"),
        PermitAllPath(HttpMethod.PUT, "/auth/reissue"),
        PermitAllPath(HttpMethod.POST, "/auth/passport"),

        // students
        PermitAllPath(HttpMethod.GET, "/students/email/duplication"),
        PermitAllPath(HttpMethod.GET, "/students/account-id/duplication"),
        PermitAllPath(HttpMethod.GET, "/students/account-id/{school-id}"),
        PermitAllPath(HttpMethod.GET, "/students/name"),
        PermitAllPath(HttpMethod.POST, "/students/signup"),
        PermitAllPath(HttpMethod.PATCH, "/students/password/initialization"),

        // managers
        PermitAllPath(HttpMethod.GET, "/managers/account-id/{school-id}"),
        PermitAllPath(HttpMethod.PATCH, "/managers/password/initialization"),

        // schools
        PermitAllPath(HttpMethod.GET, "/schools"),
        PermitAllPath(HttpMethod.GET, "/schools/question/{school-id}"),
        PermitAllPath(HttpMethod.GET, "/schools/answer/{school-id}"),
        PermitAllPath(HttpMethod.GET, "/schools/code"),

        // files
        PermitAllPath(HttpMethod.POST, "/files"),
        PermitAllPath(HttpMethod.GET, "/files/url"),

        // templates (모든 메서드 permitAll)
        PermitAllPath(null, "/templates"),
    )
}
