package team.aliens.dms.domain.auth.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.stub.createAuthCodeStub

class AuthCodeTest : DescribeSpec({

    describe("validateAuthCode") {
        val authCode = createAuthCodeStub()

        context("인증 코드가 같다면") {
            val code = "123546"

            it("인증에 성공한다") {
                shouldNotThrowAny {
                    authCode.validateAuthCode(code)
                }
            }
        }

        context("인증 코드가 다르면") {
            val wrongAuthCOde = "000000"

            it("인증에 실패한다") {
                shouldThrow<AuthCodeMismatchException> {
                    authCode.validateAuthCode(wrongAuthCOde)
                }
            }
        }
    }
})
