package team.aliens.dms.domain.meal.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.meal.stub.createMealStub

class MealTest : DescribeSpec({

    describe("toSplit") {
        context("급식이 정상적으로 입력되면") {
            val meal = createMealStub()
            val mealString = "밥||김치||된장찌개"

            val result = meal.toSplit(mealString)

            it("||를 기준으로 분리된 리스트를 반환한다") {
                result shouldBe listOf("밥", "김치", "된장찌개")
            }
        }

        context("급식이 null이면") {
            val meal = createMealStub()

            val result = meal.toSplit(null)

            it("빈 급식 메시지를 반환한다") {
                result shouldBe listOf(Meal.emptyMeal)
            }
        }

        context("급식이 빈 문자열이면") {
            val meal = createMealStub()

            val result = meal.toSplit("")

            it("빈 급식 메시지를 반환한다") {
                result shouldBe listOf(Meal.emptyMeal)
            }
        }

        context("구분자가 없는 급식이면") {
            val meal = createMealStub()
            val mealString = "밥"

            val result = meal.toSplit(mealString)

            it("단일 항목 리스트를 반환한다") {
                result shouldBe listOf("밥")
            }
        }

        context("여러 구분자가 있는 급식이면") {
            val meal = createMealStub()
            val mealString = "밥||김치||된장찌개||과일||샐러드"

            val result = meal.toSplit(mealString)

            it("모든 항목이 분리된 리스트를 반환한다") {
                result shouldBe listOf("밥", "김치", "된장찌개", "과일", "샐러드")
            }
        }
    }
})
