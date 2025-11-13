package team.aliens.dms.domain.auth.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

class CheckAccountIdExistenceUseCaseTest : DescribeSpec({

    val userService = mockk<UserService>()
    val checkAccountIdExistenceUseCase = CheckAccountIdExistenceUseCase(userService)

    describe("execute") {
        val accountId = "student123"
        val schoolId = UUID.randomUUID()

        context("사용자가 존재하면") {
            val email = "student@example.com"
            val user = User(
                id = UUID.randomUUID(),
                schoolId = schoolId,
                accountId = accountId,
                password = "encodedPassword",
                email = email,
                authority = Authority.STUDENT
            )

            every { userService.queryUserByAccountId(accountId) } returns user

            it("이메일 일부를 마스킹하여 반환한다") {
                val result = checkAccountIdExistenceUseCase.execute(accountId)

                result shouldContain "*"
                result shouldContain "@example.com"
            }
        }

        context("짧은 이메일 주소를 가진 사용자가 존재하면") {
            val email = "abc@test.com"
            val user = User(
                id = UUID.randomUUID(),
                schoolId = schoolId,
                accountId = accountId,
                password = "encodedPassword",
                email = email,
                authority = Authority.STUDENT
            )

            every { userService.queryUserByAccountId(accountId) } returns user

            it("이메일 일부를 마스킹하여 반환한다") {
                val result = checkAccountIdExistenceUseCase.execute(accountId)

                result shouldContain "*"
                result shouldContain "@test.com"
            }
        }

        context("긴 이메일 주소를 가진 사용자가 존재하면") {
            val email = "verylongemailaddress@example.com"
            val user = User(
                id = UUID.randomUUID(),
                schoolId = schoolId,
                accountId = accountId,
                password = "encodedPassword",
                email = email,
                authority = Authority.STUDENT
            )

            every { userService.queryUserByAccountId(accountId) } returns user

            it("이메일 일부를 마스킹하여 반환한다") {
                val result = checkAccountIdExistenceUseCase.execute(accountId)

                result shouldContain "*"
                result shouldContain "@example.com"
            }
        }
    }
})
