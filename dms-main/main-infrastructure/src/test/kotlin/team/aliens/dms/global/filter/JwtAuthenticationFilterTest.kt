package team.aliens.dms.global.filter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.DispatcherType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.PassportUser
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.global.security.principle.CustomDetails
import team.aliens.dms.global.security.principle.GeneralTeacherDetails
import team.aliens.dms.global.security.principle.HeadTeacherDetails
import team.aliens.dms.global.security.principle.ManagerDetails
import team.aliens.dms.global.security.principle.StudentDetails
import team.aliens.dms.global.security.token.JwtParser
import team.aliens.dms.global.security.token.JwtProperties
import java.util.UUID
import kotlin.reflect.KClass

private fun mockRequest(uri: String, authorizationHeader: String?): HttpServletRequest {
    val request = mockk<HttpServletRequest>(relaxed = true)
    every { request.requestURI } returns uri
    every { request.dispatcherType } returns DispatcherType.REQUEST
    every { request.getHeader(JwtProperties.HEADER) } returns authorizationHeader
    every { request.getAttribute(any()) } returns null
    return request
}

class JwtAuthenticationFilterTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    afterTest { SecurityContextHolder.clearContext() }

    val jwtParser = mockk<JwtParser>()
    val filter = JwtAuthenticationFilter(jwtParser)
    val response = mockk<HttpServletResponse>(relaxed = true)
    val filterChain = mockk<FilterChain>(relaxed = true)

    describe("doFilter") {
        context("permitAll 경로면") {
            val request = mockRequest(uri = "/auth/tokens", authorizationHeader = null)

            it("JWT 검증 없이 다음 필터로 넘긴다") {
                filter.doFilter(request, response, filterChain)

                verify(exactly = 0) { jwtParser.extractUserInfo(any()) }
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }

        context("인증이 필요한 경로인데 Authorization 헤더가 없으면") {
            val request = mockRequest(uri = "/students", authorizationHeader = null)

            it("InvalidTokenException을 던지고 다음 필터로 넘어가지 않는다") {
                shouldThrow<InvalidTokenException> {
                    filter.doFilter(request, response, filterChain)
                }
                verify(exactly = 0) { filterChain.doFilter(any(), any()) }
            }
        }

        context("Bearer 프리픽스가 없으면") {
            val request = mockRequest(uri = "/students", authorizationHeader = "raw-token-without-prefix")

            it("InvalidTokenException을 던진다") {
                shouldThrow<InvalidTokenException> {
                    filter.doFilter(request, response, filterChain)
                }
            }
        }

        context("정상 토큰이면") {
            it("권한에 맞는 Details를 SecurityContext에 저장하고 다음 필터로 넘긴다") {
                forAll(
                    row(Authority.STUDENT, StudentDetails::class),
                    row(Authority.MANAGER, ManagerDetails::class),
                    row(Authority.GENERAL_TEACHER, GeneralTeacherDetails::class),
                    row(Authority.HEAD_TEACHER, HeadTeacherDetails::class),
                ) { authority: Authority, expectedDetailsType: KClass<out CustomDetails> ->

                    val userId = UUID.randomUUID()
                    val schoolId = UUID.randomUUID()
                    val token = "token-$authority"
                    val request = mockRequest(uri = "/students", authorizationHeader = "Bearer $token")
                    every { jwtParser.extractUserInfo(token) } returns PassportUser(
                        id = userId,
                        schoolId = schoolId,
                        authority = authority
                    )

                    filter.doFilter(request, response, filterChain)

                    val details = SecurityContextHolder.getContext().authentication.principal as CustomDetails
                    details::class shouldBe expectedDetailsType
                    details.userId shouldBe userId
                    details.schoolId shouldBe schoolId
                    details.authority shouldBe authority

                    verify(exactly = 1) { filterChain.doFilter(request, response) }

                    SecurityContextHolder.clearContext()
                }
            }
        }
    }
})
