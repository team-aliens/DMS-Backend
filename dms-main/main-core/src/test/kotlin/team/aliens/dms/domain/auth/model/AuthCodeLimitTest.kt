package team.aliens.dms.domain.auth.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.auth.exception.AuthCodeOverLimitException
import team.aliens.dms.domain.auth.stub.createAuthCodeLimitStub

class AuthCodeLimitTest : DescribeSpec({

    describe("certified") {
        context("인증을 완료하면") {
            val authCodeLimit = createAuthCodeLimitStub(
                attemptCount = 3,
                isVerified = false
            )

            val result = authCodeLimit.certified()

            it("attemptCount가 MAX_ATTEMPT_COUNT로 설정된다") {
                result.attemptCount shouldBe AuthCodeLimit.MAX_ATTEMPT_COUNT
            }

            it("isVerified가 true로 설정된다") {
                result.isVerified shouldBe true
            }

            it("expirationTime이 증가한다") {
                result.expirationTime shouldBe 2700
            }
        }
    }

    describe("increaseCount") {
        context("시도 횟수가 MAX_ATTEMPT_COUNT 미만이면") {
            val authCodeLimit = createAuthCodeLimitStub(attemptCount = 3)

            val result = authCodeLimit.increaseCount()

            it("attemptCount가 1 증가한다") {
                result.attemptCount shouldBe 4
            }
        }

        context("시도 횟수가 MAX_ATTEMPT_COUNT에 도달하면") {
            val authCodeLimit = createAuthCodeLimitStub(attemptCount = AuthCodeLimit.MAX_ATTEMPT_COUNT)

            it("AuthCodeOverLimitException을 발생시킨다") {
                shouldThrow<AuthCodeOverLimitException> {
                    authCodeLimit.increaseCount()
                }
            }
        }

        context("시도 횟수가 MAX_ATTEMPT_COUNT를 초과하면") {
            val authCodeLimit = createAuthCodeLimitStub(attemptCount = AuthCodeLimit.MAX_ATTEMPT_COUNT + 1)

            it("AuthCodeOverLimitException을 발생시킨다") {
                shouldThrow<AuthCodeOverLimitException> {
                    authCodeLimit.increaseCount()
                }
            }
        }
    }
})
