package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.StudyRoomFacade
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

class ManagerQueryStudyRoomsUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val studyRoomFacade: StudyRoomFacade = mockk(relaxed = true)

    private val managerQueryRoomsUseCase = ManagerQueryStudyRoomsUseCase(
        securityPort, queryUserPort, queryStudyRoomPort, studyRoomFacade
    )

    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val password = "test password"
    private val timeSlotId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = managerId,
            schoolId = schoolId,
            accountId = "test account id",
            password = password,
            email = "test email",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studyRoomStub by lazy {
        StudyRoomVO(
            id = UUID.randomUUID(),
            floor = 1,
            name = "",
            availableGrade = 0,
            availableSex = Sex.FEMALE,
            inUseHeadcount = 3,
            totalAvailableSeat = 1,
        )
    }

    @Test
    fun `자습실 목록 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every { queryStudyRoomPort.queryAllStudyRoomsByTimeSlotId(timeSlotId) } returns listOf(studyRoomStub)

        // when & then
        assertDoesNotThrow {
            managerQueryRoomsUseCase.execute(timeSlotId)
        }
    }

    @Test
    fun `자습실 생성 성공 (timeSlotId null인 경우)`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every { queryStudyRoomPort.queryAllStudyRoomsByTimeSlotId(timeSlotId) } returns listOf(studyRoomStub)

        // when & then
        assertDoesNotThrow {
            managerQueryRoomsUseCase.execute(null)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            managerQueryRoomsUseCase.execute(timeSlotId)
        }
    }
}
