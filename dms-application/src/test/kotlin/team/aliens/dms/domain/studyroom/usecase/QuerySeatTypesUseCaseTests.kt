package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.stub.createSeatTypeStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub

class QuerySeatTypesUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val querySeatTypePort: QuerySeatTypePort = mockk(relaxed = true)

    private val querySeatTypesUseCase = QuerySeatTypesUseCase(
        securityPort, queryUserPort, querySeatTypePort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId,
            schoolId = schoolId
        )
    }

    private val seatTypesStub by lazy {
        listOf(createSeatTypeStub())
    }

    @Test
    fun `자리 종류 조회 성공 (studyRoomId가 null인 경우)`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { querySeatTypePort.queryAllSeatTypeBySchoolId(schoolId) } returns seatTypesStub

        // when
        val response = querySeatTypesUseCase.execute(null)

        // then
        assertAll(
            { verify(exactly = 1) { querySeatTypePort.queryAllSeatTypeBySchoolId(schoolId) } },
            { assertEquals(response.types.size, seatTypesStub.size) }
        )
    }

    @Test
    fun `자리 종류 조회 성공 (studyRoomId가 null이 아닌 경우)`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { querySeatTypePort.queryAllSeatTypeByStudyRoomId(studyRoomId) } returns seatTypesStub

        // when
        val response = querySeatTypesUseCase.execute(studyRoomId)

        // then
        assertAll(
            { verify(exactly = 1) { querySeatTypePort.queryAllSeatTypeByStudyRoomId(studyRoomId) } },
            { assertEquals(response.types.size, seatTypesStub.size) }
        )
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when
        assertThrows<UserNotFoundException> {
            querySeatTypesUseCase.execute(null)
        }
    }
}