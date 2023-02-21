package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointQueryManagerPort
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.point.spi.vo.PointHistoryVO
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryStudentPointHistoryUseCaseTest {

    private val securityPort: PointSecurityPort = mockk()
    private val queryManagerPort: PointQueryManagerPort = mockk()
    private val queryStudentPort: PointQueryStudentPort = mockk()
    private val queryPointHistoryPort: QueryPointHistoryPort = mockk()

    private val queryStudentPointHistoryUseCase = QueryStudentPointHistoryUseCase(
        securityPort, queryManagerPort, queryStudentPort, queryPointHistoryPort
    )

    private val currentUserId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val roomId = UUID.randomUUID()
    private val managerId = currentUserId
    private val schoolId = UUID.randomUUID()
    private val otherSchoolId = UUID.randomUUID()

    private val managerStub by lazy {
        Manager(
            id = managerId,
            schoolId = schoolId,
            name = "사감쌤"
        )
    }

    private val studentStub by lazy {
        Student(
            id = studentId,
            schoolId = schoolId,
            name = "이하성",
            grade = 2,
            classRoom = 1,
            number = 15,
            roomId = roomId,
            sex = Sex.MALE,
            roomNumber = 422
        )
    }

    private val otherSchoolStudentStub by lazy {
        Student(
            id = studentId,
            schoolId = otherSchoolId,
            name = "홍길동",
            grade = 1,
            classRoom = 1,
            number = 11,
            roomId = roomId,
            sex = Sex.MALE,
            roomNumber = 422
        )
    }

    private val pointHistoryStub by lazy {
        PointHistoryVO(
            name = "호실 청결상태 우수",
            type = PointType.BONUS,
            score = 10,
            date = LocalDate.now()
        )
    }

    private val pointHistoryStub2 by lazy {
        PointHistoryVO(
            name = "타호실",
            type = PointType.MINUS,
            score = 10,
            date = LocalDate.now()
        )
    }

    private val pageStub by lazy {
        PageData(
            page = 0,
            size = 10
        )
    }

    @Test
    fun `학생 상벌점 내역 조회 성공`() {
        //given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(currentUserId) } returns managerStub

        every { queryStudentPort.queryStudentById(studentId) } returns studentStub

        every {
            queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
                gcn = studentStub.gcn,
                studentName = studentStub.name,
                isCancel = false,
                pageData = pageStub
            )
        } returns listOf(pointHistoryStub, pointHistoryStub2)

        //when
        val result = queryStudentPointHistoryUseCase.execute(studentId, pageStub).pointHistories

        //then
        assertAll(
            { assertEquals(result.size, 2) },
            { assertEquals(result[0], pointHistoryStub) }
        )
    }

    @Test
    fun `학생 미존재`() {
        //given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(currentUserId) } returns managerStub

        every { queryStudentPort.queryStudentById(studentId) } returns null
        
        every {
            queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
                gcn = studentStub.gcn,
                studentName = studentStub.name,
                isCancel = false,
                pageData = pageStub
            )
        } returns listOf(pointHistoryStub, pointHistoryStub2)

        //when & then
        assertThrows<StudentNotFoundException> {
            queryStudentPointHistoryUseCase.execute(studentId, pageStub)
        }
    }

    @Test
    fun `다른 학교 학생인경우`() {
        //given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(currentUserId) } returns managerStub

        every { queryStudentPort.queryStudentById(studentId) } returns otherSchoolStudentStub

        every {
            queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
                gcn = studentStub.gcn,
                studentName = studentStub.name,
                isCancel = false,
                pageData = pageStub
            )
        } returns listOf(pointHistoryStub, pointHistoryStub2)

        //when & then
        assertThrows<SchoolMismatchException> {
            queryStudentPointHistoryUseCase.execute(studentId, pageStub)
        }
    }

    @Test
    fun `관리자 미존재`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryManagerPort.queryManagerById(managerId) } returns null

        // when & then
        assertThrows<ManagerNotFoundException> {
            queryStudentPointHistoryUseCase.execute(studentId, pageStub)
        }
    }
}