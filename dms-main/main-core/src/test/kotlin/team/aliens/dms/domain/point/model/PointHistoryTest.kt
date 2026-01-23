package team.aliens.dms.domain.point.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.exception.PointHistoryCanNotCancelException
import team.aliens.dms.domain.point.stub.createPointHistoryStub

class PointHistoryTest : DescribeSpec({

    describe("getTotalPoint") {
        context("타입이 BONUS이면") {
            val result = PointHistory.getTotalPoint(
                type = PointRequestType.BONUS,
                bonusTotal = 10,
                minusTotal = 5
            )

            it("bonusTotal을 반환한다") {
                result shouldBe 10
            }
        }

        context("타입이 MINUS이면") {
            val result = PointHistory.getTotalPoint(
                type = PointRequestType.MINUS,
                bonusTotal = 10,
                minusTotal = 5
            )

            it("minusTotal을 반환한다") {
                result shouldBe 5
            }
        }

        context("타입이 ALL이면") {
            val result = PointHistory.getTotalPoint(
                type = PointRequestType.ALL,
                bonusTotal = 10,
                minusTotal = 5
            )

            it("bonusTotal - minusTotal을 반환한다") {
                result shouldBe 5
            }
        }
    }

    describe("cancelHistory") {
        context("취소하지 않은 상점 이력을 취소하면") {
            val pointHistory = createPointHistoryStub(
                pointType = PointType.BONUS,
                pointScore = 3,
                isCancel = false
            )
            val pointTotal = Pair(10, 5)

            val result = pointHistory.cancelHistory(pointTotal)

            it("isCancel이 true로 변경된다") {
                result.isCancel shouldBe true
            }

            it("bonusTotal이 감소한다") {
                result.bonusTotal shouldBe 7
            }

            it("minusTotal은 변경되지 않는다") {
                result.minusTotal shouldBe 5
            }
        }

        context("취소하지 않은 벌점 이력을 취소하면") {
            val pointHistory = createPointHistoryStub(
                pointType = PointType.MINUS,
                pointScore = 2,
                isCancel = false
            )
            val pointTotal = Pair(10, 5)

            val result = pointHistory.cancelHistory(pointTotal)

            it("isCancel이 true로 변경된다") {
                result.isCancel shouldBe true
            }

            it("bonusTotal은 변경되지 않는다") {
                result.bonusTotal shouldBe 10
            }

            it("minusTotal이 감소한다") {
                result.minusTotal shouldBe 3
            }
        }

        context("이미 취소된 이력을 취소하면") {
            val pointHistory = createPointHistoryStub(isCancel = true)
            val pointTotal = Pair(10, 5)

            it("PointHistoryCanNotCancelException을 발생시킨다") {
                shouldThrow<PointHistoryCanNotCancelException> {
                    pointHistory.cancelHistory(pointTotal)
                }
            }
        }
    }

    describe("getTitle") {
        context("포인트 타입이 BONUS이면") {
            val pointHistory = createPointHistoryStub(pointType = PointType.BONUS)

            val result = pointHistory.getTitle()

            it("상점 부과 메시지를 반환한다") {
                result shouldBe "상점이 부과되었습니다."
            }
        }

        context("포인트 타입이 MINUS이면") {
            val pointHistory = createPointHistoryStub(pointType = PointType.MINUS)

            val result = pointHistory.getTitle()

            it("벌점 부과 메시지를 반환한다") {
                result shouldBe "벌점이 부과되었습니다."
            }
        }
    }
})
