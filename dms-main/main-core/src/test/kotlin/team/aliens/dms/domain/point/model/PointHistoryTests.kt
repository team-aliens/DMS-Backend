package team.aliens.dms.domain.point.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.exception.PointHistoryCanNotCancelException
import team.aliens.dms.domain.point.stub.createPointHistoryStub

class PointHistoryTests : DescribeSpec({

    describe("cancelHistory") {
        val pointTotal = Pair(6, 5)

        context("상벌점 내역을 취소하지 않았다면") {
            val pointHistory = createPointHistoryStub()

            val canceledPointHistory = pointHistory.cancelHistory(pointTotal)

            it("상벌점 내역을 취소 후 반환한다") {
                canceledPointHistory.isCancel shouldBe true
            }
        }

        context("상벌점 내역을 이미 취소했다면") {
            val pointHistory = createPointHistoryStub(isCancel = true)

            it("예외가 발생한다") {
                shouldThrow<PointHistoryCanNotCancelException> {
                    pointHistory.cancelHistory(pointTotal)
                }
            }
        }
    }

    describe("getTotalPoint") {
        val bonusTotal = 10
        val minusTotal = 0

        context("상벌점 타입이 BONUS이면") {
            val pointType = PointRequestType.BONUS

            val totalPoint = PointHistory.getTotalPoint(
                type = pointType,
                bonusTotal = bonusTotal,
                minusTotal = minusTotal
            )

            it("상점만 반환한다") {
                totalPoint shouldBe bonusTotal
            }
        }

        context("상벌점 타입이 MINUS이면") {
            val pointType = PointRequestType.MINUS

            val totalPoint = PointHistory.getTotalPoint(
                type = pointType,
                bonusTotal = bonusTotal,
                minusTotal = minusTotal
            )

            it("벌점만 반환한다") {
                totalPoint shouldBe minusTotal
            }
        }

        context("상벌점 타입이 ALL이면") {
            val pointType = PointRequestType.ALL

            val totalPoint = PointHistory.getTotalPoint(
                type = pointType,
                bonusTotal = bonusTotal,
                minusTotal = minusTotal
            )

            it("상벌점 총합을 반환한다") {
                totalPoint shouldBe bonusTotal + minusTotal
            }
        }
    }
})
