package team.aliens.dms.domain.tag.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.point.spi.vo.StudentTotalVO
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.WarningTag
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO
import team.aliens.dms.domain.tag.stub.createTagStub
import java.util.UUID

class UpdateStudentTagsUseCaseTest : DescribeSpec({

    val pointService = mockk<PointService>()
    val tagService = mockk<TagService>()
    val useCase = UpdateStudentTagsUseCase(pointService, tagService)

    val schoolId = UUID.randomUUID()
    val studentId = UUID.randomUUID()

    val firstWarningTagId = UUID.randomUUID()
    val secondWarningTagId = UUID.randomUUID()
    val thirdWarningTagId = UUID.randomUUID()
    val cFirstWarningTagId = UUID.randomUUID()
    val cSecondWarningTagId = UUID.randomUUID()

    val warningTagEntities = listOf(
        createTagStub(id = firstWarningTagId,   name = WarningTag.FIRST_WARNING.warningMessage,    schoolId = schoolId),
        createTagStub(id = cFirstWarningTagId,  name = WarningTag.C_FIRST_WARNING.warningMessage,  schoolId = schoolId),
        createTagStub(id = secondWarningTagId,  name = WarningTag.SECOND_WARNING.warningMessage,   schoolId = schoolId),
        createTagStub(id = cSecondWarningTagId, name = WarningTag.C_SECOND_WARNING.warningMessage, schoolId = schoolId),
        createTagStub(id = thirdWarningTagId,   name = WarningTag.THIRD_WARNING.warningMessage,    schoolId = schoolId),
    )

    fun mockSaveAndDelete() {
        every { tagService.deleteAllStudentTagsByStudentIdIn(any()) } just runs
        every { tagService.saveAllStudentTags(any()) } just runs
    }

    describe("execute") {

        context("감점이 기준 미만(SAFE)인 학생만 있을 때") {
            every { tagService.getTagsByTagNameIn(any()) } returns warningTagEntities
            every { tagService.getAllStudentTagDetails() } returns emptyList()
            every { pointService.getPointTotalsGroupByStudent() } returns listOf(
                StudentTotalVO(studentId, bonusTotal = 0, minusTotal = 10)
            )
            mockSaveAndDelete()

            it("아무 태그도 저장하지 않는다") {
                val savedTags = slot<List<StudentTag>>()
                every { tagService.saveAllStudentTags(capture(savedTags)) } just runs

                shouldNotThrowAny { useCase.execute() }
                savedTags.captured.size shouldBe 0
            }
        }

        context("경고 태그가 없는 학생이 감점 15점(경고 1단계 기준)일 때") {
            every { tagService.getTagsByTagNameIn(any()) } returns warningTagEntities
            every { tagService.getAllStudentTagDetails() } returns emptyList()
            every { pointService.getPointTotalsGroupByStudent() } returns listOf(
                StudentTotalVO(studentId, bonusTotal = 0, minusTotal = 15)
            )
            mockSaveAndDelete()

            it("경고 1단계 태그가 저장된다") {
                val savedTags = slot<List<StudentTag>>()
                every { tagService.saveAllStudentTags(capture(savedTags)) } just runs

                useCase.execute()

                savedTags.captured.size shouldBe 1
                savedTags.captured[0].tagId shouldBe firstWarningTagId
            }
        }

        context("경고 1단계(완료) 태그를 가진 학생이 감점 20점(경고 2단계 기준)일 때") {
            every { tagService.getTagsByTagNameIn(any()) } returns warningTagEntities
            every { tagService.getAllStudentTagDetails() } returns listOf(
                StudentTagDetailVO(
                    studentId = studentId,
                    studentName = "홍길동",
                    tagId = cFirstWarningTagId,
                    tagColor = "#FF4646",
                    tagName = WarningTag.C_FIRST_WARNING.warningMessage
                )
            )
            every { pointService.getPointTotalsGroupByStudent() } returns listOf(
                StudentTotalVO(studentId, bonusTotal = 0, minusTotal = 20)
            )
            mockSaveAndDelete()

            it("경고 2단계 태그로 업그레이드된다") {
                val savedTags = slot<List<StudentTag>>()
                every { tagService.saveAllStudentTags(capture(savedTags)) } just runs

                useCase.execute()

                savedTags.captured.size shouldBe 1
                savedTags.captured[0].tagId shouldBe secondWarningTagId
            }
        }

        context("경고 1단계(완료) 태그를 가진 학생이 아직 감점 15점(경고 2단계 미만)일 때") {
            every { tagService.getTagsByTagNameIn(any()) } returns warningTagEntities
            every { tagService.getAllStudentTagDetails() } returns listOf(
                StudentTagDetailVO(
                    studentId = studentId,
                    studentName = "홍길동",
                    tagId = cFirstWarningTagId,
                    tagColor = "#FF4646",
                    tagName = WarningTag.C_FIRST_WARNING.warningMessage
                )
            )
            every { pointService.getPointTotalsGroupByStudent() } returns listOf(
                StudentTotalVO(studentId, bonusTotal = 0, minusTotal = 15)
            )
            mockSaveAndDelete()

            it("업그레이드하지 않는다") {
                val savedTags = slot<List<StudentTag>>()
                every { tagService.saveAllStudentTags(capture(savedTags)) } just runs

                useCase.execute()

                savedTags.captured.size shouldBe 0
            }
        }

        context("경고 1단계(미완료) 태그를 가진 학생이 감점 20점일 때") {
            every { tagService.getTagsByTagNameIn(any()) } returns warningTagEntities
            every { tagService.getAllStudentTagDetails() } returns listOf(
                StudentTagDetailVO(
                    studentId = studentId,
                    studentName = "홍길동",
                    tagId = firstWarningTagId,
                    tagColor = "#FF4646",
                    tagName = WarningTag.FIRST_WARNING.warningMessage
                )
            )
            every { pointService.getPointTotalsGroupByStudent() } returns listOf(
                StudentTotalVO(studentId, bonusTotal = 0, minusTotal = 20)
            )
            mockSaveAndDelete()

            it("처벌 미이행이므로 업그레이드하지 않는다") {
                val savedTags = slot<List<StudentTag>>()
                every { tagService.saveAllStudentTags(capture(savedTags)) } just runs

                useCase.execute()

                savedTags.captured.size shouldBe 0
            }
        }

        context("일반 태그(경고 태그 아님)만 가진 학생이 감점 15점일 때") {
            val generalTagId = UUID.randomUUID()
            every { tagService.getTagsByTagNameIn(any()) } returns warningTagEntities
            every { tagService.getAllStudentTagDetails() } returns listOf(
                StudentTagDetailVO(
                    studentId = studentId,
                    studentName = "홍길동",
                    tagId = generalTagId,
                    tagColor = "#0000FF",
                    tagName = "소프트웨어개발과"
                )
            )
            every { pointService.getPointTotalsGroupByStudent() } returns listOf(
                StudentTotalVO(studentId, bonusTotal = 0, minusTotal = 15)
            )
            mockSaveAndDelete()

            it("TagNotFoundException 없이 경고 1단계 태그가 저장된다") {
                val savedTags = slot<List<StudentTag>>()
                every { tagService.saveAllStudentTags(capture(savedTags)) } just runs

                shouldNotThrowAny { useCase.execute() }
                savedTags.captured.size shouldBe 1
                savedTags.captured[0].tagId shouldBe firstWarningTagId
            }
        }
    }
})
