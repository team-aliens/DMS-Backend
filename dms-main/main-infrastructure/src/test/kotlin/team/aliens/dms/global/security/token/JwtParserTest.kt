package team.aliens.dms.global.security.token

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.global.security.SecurityProperties
import team.aliens.dms.global.security.exception.ExpiredTokenException
import team.aliens.dms.global.security.exception.InvalidTokenException
import java.util.Date
import java.util.UUID

private fun buildToken(
    securityProperties: SecurityProperties,
    type: String = JwtProperties.ACCESS,
    userId: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    authority: String? = Authority.STUDENT.name,
    expiration: Date = Date(System.currentTimeMillis() + 3_600_000)
): String {
    val builder = Jwts.builder()
        .signWith(securityProperties.secretKey, SignatureAlgorithm.HS512)
        .setHeaderParam(Header.TYPE, type)
        .setId(userId.toString())
        .claim(JwtProperties.SCHOOL_ID, schoolId.toString())
        .setIssuedAt(Date())
        .setExpiration(expiration)

    if (authority != null) {
        builder.claim(JwtProperties.AUTHORITY, authority)
    }

    return builder.compact()
}

class JwtParserTest : DescribeSpec({

    val securityProperties = SecurityProperties(
        secretKey = "test-jwt-secret-key-".repeat(4),
        accessExp = 3600,
        refreshExp = 1_205_600
    )
    val jwtParser = JwtParser(securityProperties)

    describe("extractUserInfo") {
        context("유효한 access 토큰이면") {
            val userId = UUID.randomUUID()
            val schoolId = UUID.randomUUID()
            val token = buildToken(
                securityProperties,
                userId = userId,
                schoolId = schoolId,
                authority = Authority.MANAGER.name
            )

            it("토큰의 클레임으로 PassportUser를 만든다") {
                val result = jwtParser.extractUserInfo(token)

                result.id shouldBe userId
                result.schoolId shouldBe schoolId
                result.authority shouldBe Authority.MANAGER
            }
        }

        context("만료된 토큰이면") {
            val token = buildToken(securityProperties, expiration = Date(System.currentTimeMillis() - 1_000))

            it("ExpiredTokenException을 던진다") {
                shouldThrow<ExpiredTokenException> {
                    jwtParser.extractUserInfo(token)
                }
            }
        }

        context("다른 시크릿으로 서명된(위조된) 토큰이면") {
            val forgedProperties = SecurityProperties(
                secretKey = "forged-jwt-secret-key-".repeat(4),
                accessExp = 3600,
                refreshExp = 1_205_600
            )
            val token = buildToken(forgedProperties)

            it("InvalidTokenException을 던진다") {
                shouldThrow<InvalidTokenException> {
                    jwtParser.extractUserInfo(token)
                }
            }
        }

        context("refresh 타입 토큰이면") {
            val token = buildToken(securityProperties, type = JwtProperties.REFRESH)

            it("InvalidTokenException을 던진다") {
                shouldThrow<InvalidTokenException> {
                    jwtParser.extractUserInfo(token)
                }
            }
        }

        context("authority 클레임이 유효하지 않은 값이면") {
            val token = buildToken(securityProperties, authority = "NOT_A_REAL_AUTHORITY")

            it("InvalidTokenException을 던진다") {
                shouldThrow<InvalidTokenException> {
                    jwtParser.extractUserInfo(token)
                }
            }
        }

        context("authority 클레임이 아예 없으면") {
            val token = buildToken(securityProperties, authority = null)

            it("InvalidTokenException을 던진다") {
                shouldThrow<InvalidTokenException> {
                    jwtParser.extractUserInfo(token)
                }
            }
        }
    }
})
