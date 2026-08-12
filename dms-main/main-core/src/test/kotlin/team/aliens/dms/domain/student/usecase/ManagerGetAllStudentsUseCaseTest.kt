package team.aliens.dms.domain.student.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.stub.createStudentWithTagStub
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.point.spi.vo.StudentTotalVO
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

class ManagerGetAllStudentsUseCaseTest : DescribeSpec({

    val userService = mockk<UserService>()
    val studentService = mockk<StudentService>()
    val pointService = mockk<PointService>()
    val useCase = ManagerGetAllStudentsUseCase(userService, studentService, pointService)

    val schoolId = UUID.randomUUID()
    val user = createUserStub(schoolId = schoolId)

    val firstStudentId = UUID.randomUUID()
    val secondStudentId = UUID.randomUUID()
    val thirdStudentId = UUID.randomUUID()

    val noPointFilter = PointFilter(filterType = null, minPoint = null, maxPoint = null)

    describe("execute") {

        context("학생마다 상벌점 이력이 있으면") {
            it("studentId를 기준으로 총점이 매칭된다") {
                clearAllMocks()
                every { userService.getCurrentUser() } returns user
                every {
                    studentService.getStudentsByNameAndSortAndTag(
                        name = null,
                        sort = Sort.GCN,
                        schoolId = schoolId,
                        tagIds = null
                    )
                } returns listOf(
                    createStudentWithTagStub(id = firstStudentId, name = "김철수"),
                    createStudentWithTagStub(id = secondStudentId, name = "박영희")
                )
                every { pointService.getPointTotalsGroupByStudent(schoolId) } returns listOf(
                    StudentTotalVO(secondStudentId, bonusTotal = 7, minusTotal = 3),
                    StudentTotalVO(firstStudentId, bonusTotal = 10, minusTotal = 5)
                )

                val response = useCase.execute(
                    name = null,
                    sort = Sort.GCN,
                    pointFilter = noPointFilter,
                    tagIds = null
                )

                response.students.map { Triple(it.name, it.bonusPoint, it.minusPoint) } shouldBe listOf(
                    Triple("김철수", 10, 5),
                    Triple("박영희", 7, 3)
                )
            }
        }

        context("상벌점 이력이 없는 학생이 섞여 있으면") {
            it("그 학생의 상벌점은 0으로 반환된다") {
                clearAllMocks()
                every { userService.getCurrentUser() } returns user
                every {
                    studentService.getStudentsByNameAndSortAndTag(
                        name = null,
                        sort = Sort.GCN,
                        schoolId = schoolId,
                        tagIds = null
                    )
                } returns listOf(
                    createStudentWithTagStub(id = firstStudentId, name = "이력있음"),
                    createStudentWithTagStub(id = secondStudentId, name = "이력없음")
                )
                every { pointService.getPointTotalsGroupByStudent(schoolId) } returns listOf(
                    StudentTotalVO(firstStudentId, bonusTotal = 10, minusTotal = 5)
                )

                val response = useCase.execute(
                    name = null,
                    sort = Sort.GCN,
                    pointFilter = noPointFilter,
                    tagIds = null
                )

                response.students.map { Triple(it.name, it.bonusPoint, it.minusPoint) } shouldBe listOf(
                    Triple("이력있음", 10, 5),
                    Triple("이력없음", 0, 0)
                )
            }
        }

        context("학생 조회 결과 순서는") {
            it("상벌점을 합친 뒤에도 그대로 유지된다") {
                clearAllMocks()
                every { userService.getCurrentUser() } returns user
                every {
                    studentService.getStudentsByNameAndSortAndTag(
                        name = null,
                        sort = Sort.NAME,
                        schoolId = schoolId,
                        tagIds = null
                    )
                } returns listOf(
                    createStudentWithTagStub(id = firstStudentId, name = "첫번째"),
                    createStudentWithTagStub(id = secondStudentId, name = "두번째"),
                    createStudentWithTagStub(id = thirdStudentId, name = "세번째")
                )
                every { pointService.getPointTotalsGroupByStudent(schoolId) } returns listOf(
                    StudentTotalVO(thirdStudentId, bonusTotal = 3, minusTotal = 0),
                    StudentTotalVO(firstStudentId, bonusTotal = 1, minusTotal = 0)
                )

                val response = useCase.execute(
                    name = null,
                    sort = Sort.NAME,
                    pointFilter = noPointFilter,
                    tagIds = null
                )

                response.students.map { it.name } shouldBe listOf("첫번째", "두번째", "세번째")
            }
        }

        context("상벌점 필터가 주어지면") {
            it("필터 타입과 범위에 맞는 학생만 반환한다") {
                forAll(
                    // 상점 10점, 벌점 5점인 학생 하나를 두고 필터만 바꿔가며 검증한다
                    row(PointFilterType.BONUS, 10, 10, 1),
                    row(PointFilterType.BONUS, 0, 9, 0),
                    row(PointFilterType.BONUS, 11, 20, 0),
                    row(PointFilterType.MINUS, 5, 5, 1),
                    row(PointFilterType.MINUS, 6, 10, 0),
                    row(PointFilterType.ALL, 5, 5, 1),
                    row(PointFilterType.ALL, 6, 10, 0)
                ) { filterType, minPoint, maxPoint, expectedSize ->
                    clearAllMocks()
                    every { userService.getCurrentUser() } returns user
                    every {
                        studentService.getStudentsByNameAndSortAndTag(
                            name = null,
                            sort = Sort.GCN,
                            schoolId = schoolId,
                            tagIds = null
                        )
                    } returns listOf(createStudentWithTagStub(id = firstStudentId))
                    every { pointService.getPointTotalsGroupByStudent(schoolId) } returns listOf(
                        StudentTotalVO(firstStudentId, bonusTotal = 10, minusTotal = 5)
                    )

                    val response = useCase.execute(
                        name = null,
                        sort = Sort.GCN,
                        pointFilter = PointFilter(filterType, minPoint, maxPoint),
                        tagIds = null
                    )

                    response.students.size shouldBe expectedSize
                }
            }
        }

        context("상벌점 이력이 없는 학생에 상점 0점 필터를 걸면") {
            it("그 학생이 포함된다") {
                clearAllMocks()
                every { userService.getCurrentUser() } returns user
                every {
                    studentService.getStudentsByNameAndSortAndTag(
                        name = null,
                        sort = Sort.GCN,
                        schoolId = schoolId,
                        tagIds = null
                    )
                } returns listOf(createStudentWithTagStub(id = firstStudentId, name = "이력없음"))
                every { pointService.getPointTotalsGroupByStudent(schoolId) } returns emptyList()

                val response = useCase.execute(
                    name = null,
                    sort = Sort.GCN,
                    pointFilter = PointFilter(PointFilterType.BONUS, 0, 0),
                    tagIds = null
                )

                response.students.map { it.name } shouldBe listOf("이력없음")
            }
        }

        context("이름과 태그로 조회하면") {
            it("조회 조건을 그대로 전달하고 현재 사용자의 학교로 상벌점을 조회한다") {
                clearAllMocks()
                val tagId = UUID.randomUUID()
                every { userService.getCurrentUser() } returns user
                every {
                    studentService.getStudentsByNameAndSortAndTag(
                        name = "김",
                        sort = Sort.NAME,
                        schoolId = schoolId,
                        tagIds = listOf(tagId)
                    )
                } returns listOf(createStudentWithTagStub(id = firstStudentId, name = "김철수"))
                every { pointService.getPointTotalsGroupByStudent(schoolId) } returns emptyList()

                useCase.execute(
                    name = "김",
                    sort = Sort.NAME,
                    pointFilter = noPointFilter,
                    tagIds = listOf(tagId)
                )

                verify(exactly = 1) {
                    studentService.getStudentsByNameAndSortAndTag(
                        name = "김",
                        sort = Sort.NAME,
                        schoolId = schoolId,
                        tagIds = listOf(tagId)
                    )
                }
                verify(exactly = 1) { pointService.getPointTotalsGroupByStudent(schoolId) }
            }
        }
    }
})
