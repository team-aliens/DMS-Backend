package team.aliens.dms.domain.manager.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.dto.ManagerMyPageResponse
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.manager.stub.createManagerStub
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.stub.createSchoolStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ManagerMyPageUseCaseTests {

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    @MockBean
    private lateinit var querySchoolPort: ManagerQuerySchoolPort

    @MockBean
    private lateinit var queryManagerPort: QueryManagerPort

    private lateinit var managerMyPageUseCase: ManagerMyPageUseCase

    @BeforeEach
    fun setUp() {
        managerMyPageUseCase = ManagerMyPageUseCase(securityPort, querySchoolPort, queryManagerPort)
    }

    private val currentManagerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val managerStub by lazy {
        createManagerStub(
            id = currentManagerId,
            schoolId = schoolId
        )
    }

    private val schoolStub by lazy {
        createSchoolStub(id = schoolId)
    }

    private val managerMyPageResponse by lazy {
        ManagerMyPageResponse(
            schoolId = schoolId,
            schoolName = "test name",
            code = "test code",
            question = "test question",
            answer = "test answer"
        )
    }

    @Test
    fun `관리자 마이페이지 확인 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentManagerId)

        given(queryManagerPort.queryManagerById(currentManagerId))
            .willReturn(managerStub)

        given(querySchoolPort.querySchoolById(managerStub.schoolId))
            .willReturn(schoolStub)

        // when
        val response = managerMyPageUseCase.execute()

        // then
        assertEquals(response, managerMyPageResponse)
    }

    @Test
    fun `사용자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentManagerId)

        given(queryManagerPort.queryManagerById(currentManagerId))
            .willReturn(null)

        // when & then
        assertThrows<ManagerNotFoundException> {
            managerMyPageUseCase.execute()
        }
    }

    @Test
    fun `학교 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentManagerId)

        given(queryManagerPort.queryManagerById(currentManagerId))
            .willReturn(managerStub)

        given(querySchoolPort.querySchoolById(managerStub.schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            managerMyPageUseCase.execute()
        }
    }
}
