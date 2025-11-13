package team.aliens.dms.domain.tag.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.tag.exception.TagNotFoundException

class WarningTagTest : DescribeSpec({

    describe("byContent") {
        context("유효한 경고 메시지가 주어지면") {
            it("해당하는 WarningTag를 반환한다") {
                WarningTag.byContent("경고 1단계") shouldBe WarningTag.FIRST_WARNING
                WarningTag.byContent("경고 2단계") shouldBe WarningTag.SECOND_WARNING
                WarningTag.byContent("경고 3단계") shouldBe WarningTag.THIRD_WARNING
                WarningTag.byContent("OUT 1") shouldBe WarningTag.ONE_OUT
                WarningTag.byContent("OUT 2") shouldBe WarningTag.TWO_OUT
                WarningTag.byContent("OUT 3") shouldBe WarningTag.THREE_OUT
            }
        }

        context("유효한 완료 상태 경고 메시지가 주어지면") {
            it("해당하는 완료 상태 WarningTag를 반환한다") {
                WarningTag.byContent("경고 1단계(완료)") shouldBe WarningTag.C_FIRST_WARNING
                WarningTag.byContent("경고 2단계(완료)") shouldBe WarningTag.C_SECOND_WARNING
                WarningTag.byContent("경고 3단계(완료)") shouldBe WarningTag.C_THIRD_WARNING
                WarningTag.byContent("OUT 1(완료)") shouldBe WarningTag.C_ONE_OUT
                WarningTag.byContent("OUT 2(완료)") shouldBe WarningTag.C_TWO_OUT
                WarningTag.byContent("OUT 3(완료)") shouldBe WarningTag.C_THREE_OUT
            }
        }

        context("존재하지 않는 경고 메시지가 주어지면") {
            it("TagNotFoundException을 발생시킨다") {
                shouldThrow<TagNotFoundException> {
                    WarningTag.byContent("존재하지 않는 경고")
                }
            }
        }
    }

    describe("byPoint") {
        context("감점 포인트가 60 이상이면") {
            it("THREE_OUT을 반환한다") {
                WarningTag.byPoint(60) shouldBe WarningTag.THREE_OUT
                WarningTag.byPoint(70) shouldBe WarningTag.THREE_OUT
            }
        }

        context("감점 포인트가 45 이상 60 미만이면") {
            it("TWO_OUT을 반환한다") {
                WarningTag.byPoint(45) shouldBe WarningTag.TWO_OUT
                WarningTag.byPoint(59) shouldBe WarningTag.TWO_OUT
            }
        }

        context("감점 포인트가 35 이상 45 미만이면") {
            it("ONE_OUT을 반환한다") {
                WarningTag.byPoint(35) shouldBe WarningTag.ONE_OUT
                WarningTag.byPoint(44) shouldBe WarningTag.ONE_OUT
            }
        }

        context("감점 포인트가 25 이상 35 미만이면") {
            it("THIRD_WARNING을 반환한다") {
                WarningTag.byPoint(25) shouldBe WarningTag.THIRD_WARNING
                WarningTag.byPoint(34) shouldBe WarningTag.THIRD_WARNING
            }
        }

        context("감점 포인트가 20 이상 25 미만이면") {
            it("SECOND_WARNING을 반환한다") {
                WarningTag.byPoint(20) shouldBe WarningTag.SECOND_WARNING
                WarningTag.byPoint(24) shouldBe WarningTag.SECOND_WARNING
            }
        }

        context("감점 포인트가 15 이상 20 미만이면") {
            it("FIRST_WARNING을 반환한다") {
                WarningTag.byPoint(15) shouldBe WarningTag.FIRST_WARNING
                WarningTag.byPoint(19) shouldBe WarningTag.FIRST_WARNING
            }
        }

        context("감점 포인트가 15 미만이면") {
            it("SAFE를 반환한다") {
                WarningTag.byPoint(0) shouldBe WarningTag.SAFE
                WarningTag.byPoint(14) shouldBe WarningTag.SAFE
            }
        }
    }

    describe("getAllMessages") {
        context("호출하면") {
            it("모든 경고 메시지 목록을 반환한다") {
                val messages = WarningTag.getAllMessages()

                messages shouldContainExactly listOf(
                    "경고 1단계",
                    "경고 2단계",
                    "경고 3단계",
                    "OUT 1",
                    "OUT 2",
                    "OUT 3",
                    "경고 1단계(완료)",
                    "경고 2단계(완료)",
                    "경고 3단계(완료)",
                    "OUT 1(완료)",
                    "OUT 2(완료)",
                    "OUT 3(완료)"
                )
            }
        }
    }

    describe("nextLevel") {
        context("현재 경고 단계에서 호출하면") {
            it("다음 단계 경고를 반환한다") {
                WarningTag.SAFE.nextLevel() shouldBe WarningTag.FIRST_WARNING
                WarningTag.FIRST_WARNING.nextLevel() shouldBe WarningTag.C_FIRST_WARNING
                WarningTag.C_FIRST_WARNING.nextLevel() shouldBe WarningTag.SECOND_WARNING
                WarningTag.SECOND_WARNING.nextLevel() shouldBe WarningTag.C_SECOND_WARNING
                WarningTag.C_SECOND_WARNING.nextLevel() shouldBe WarningTag.THIRD_WARNING
            }
        }
    }
})
