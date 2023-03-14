package team.aliens.dms.domain.studyroom

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import java.util.UUID

class StudyRoomFacadeTests {

    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)

    private val studyRoomFacade = StudyRoomFacade(
        queryStudyRoomPort
    )

    val schoolId = UUID.randomUUID()
    val timeSlotId = UUID.randomUUID()

    @Test
    fun `validateNullableTimeSlotId - timeSlotId가 null이 아니고 일치하는 객체가 존재하는 경우`() {
        // given
        every { queryStudyRoomPort.existsTimeSlotById(timeSlotId) } returns true

        // when & then
        assertDoesNotThrow {
            studyRoomFacade.validateNullableTimeSlotId(timeSlotId, schoolId)
        }
    }

    @Test
    fun `validateNullableTimeSlotId - timeSlotId가 null이 아니고 일치하는 객체가 존재하지 않는 경우`() {
        // given
        every { queryStudyRoomPort.existsTimeSlotById(timeSlotId) } returns false

        // when & then
        assertThrows<StudyRoomTimeSlotNotFoundException> {
            studyRoomFacade.validateNullableTimeSlotId(timeSlotId, schoolId)
        }
    }

    @Test
    fun `validateNullableTimeSlotId - timeSlotId가 null이고 같은 학교의 이용시간이 존재하지 않는 경우`() {
        // given
        every { queryStudyRoomPort.existsTimeSlotsBySchoolId(schoolId) } returns false

        // when & then
        assertDoesNotThrow {
            studyRoomFacade.validateNullableTimeSlotId(null, schoolId)
        }
    }

    @Test
    fun `validateNullableTimeSlotId - timeSlotId가 null이고 같은 학교의 이용시간이 존재하는 경우`() {
        // given
        every { queryStudyRoomPort.existsTimeSlotsBySchoolId(schoolId) } returns true

        // when & then
        assertThrows<StudyRoomTimeSlotNotFoundException> {
            studyRoomFacade.validateNullableTimeSlotId(null, schoolId)
        }
    }
}