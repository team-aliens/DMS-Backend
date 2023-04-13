package team.aliens.dms.domain.manager.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.manager.stub.createManagerStub
import team.aliens.dms.domain.point.exception.InvalidPointFilterRangeException
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.tag.stub.createTagStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryStudentsUseCaseTests {

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    @MockBean
    private lateinit var queryManagerPort: QueryManagerPort

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    private lateinit var queryStudentsUseCase: QueryStudentsUseCase

    @BeforeEach
    fun setUp() {
        queryStudentsUseCase = QueryStudentsUseCase(
            securityPort, queryManagerPort, queryStudentPort
        )
    }

    private val studentId = UUID.randomUUID()
    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val name = "name"
    private val sort = Sort.GCN
    private val filterType = PointFilterType.BONUS

    private val managerStub by lazy {
        createManagerStub(
            id = currentUserId,
            schoolId = schoolId
        )
    }

    private val studentStub by lazy {
        createStudentStub(
            id = studentId,
            schoolId = schoolId
        )
    }

    private val pointFilterStub by lazy {
        PointFilter(
            filterType = filterType,
            minPoint = 0,
            maxPoint = 10
        )
    }

    private var tagIdListStub = arrayListOf<UUID>(UUID.randomUUID())

    @Test
    fun `학생 목록 조회 성공`() {
        // given
        val studentWitTagStub = StudentWithTag(
            id = studentStub.id,
            name = studentStub.name,
            grade = studentStub.grade,
            classRoom = studentStub.classRoom,
            number = studentStub.number,
            roomNumber = studentStub.roomNumber,
            profileImageUrl = studentStub.profileImageUrl!!,
            sex = studentStub.sex,
            tags = listOf(
                createTagStub(name = "1OUT")
            )
        )

        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(
            queryStudentPort.queryStudentsByNameAndSortAndFilter(
                name, sort, managerStub.schoolId, pointFilterStub, tagIdListStub
            )
        ).willReturn(listOf(studentWitTagStub))

        // when
        val result = queryStudentsUseCase.execute(name, sort, filterType, 0, 10, tagIdListStub)

        assertThat(result).isNotNull
    }

    @Test
    fun `필터 사용시 조건 점수 입력하지 않은 경우`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        // when & then
        assertThrows<InvalidPointFilterRangeException> {
            queryStudentsUseCase.execute(
                name, sort, filterType, null, null, tagIdListStub
            )
        }
    }

    @Test
    fun `상벌점 필터 범위가 잘못 된 경우`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        // when & then
        assertThrows<InvalidPointFilterRangeException> {
            queryStudentsUseCase.execute(
                name, sort, filterType, 20, 10, tagIdListStub
            )
        }
    }

    @Test
    fun `관리자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(null)

        assertThrows<ManagerNotFoundException> {
            queryStudentsUseCase.execute(name, sort, null, null, null, tagIdListStub)
        }
    }
}
