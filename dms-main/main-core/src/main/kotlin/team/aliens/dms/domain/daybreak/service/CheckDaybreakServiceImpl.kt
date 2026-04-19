package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationAlreadyExistsException
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyTypeAlreadyExistsException
import team.aliens.dms.domain.daybreak.spi.QueryDaybreakStudyApplicationPort
import team.aliens.dms.domain.daybreak.spi.QueryDaybreakStudyTypePort
import java.util.UUID

@Service
class CheckDaybreakServiceImpl(
    private val queryDaybreakStudyApplicationPort: QueryDaybreakStudyApplicationPort,
    private val queryDaybreakStudyTypePort: QueryDaybreakStudyTypePort
) : CheckDaybreakService {

    override fun checkDaybreakStudyApplicationExists(studentId: UUID) {
        if (queryDaybreakStudyApplicationPort.existActiveDaybreakStudyApplicationByStudentId(studentId)) {
            throw DaybreakStudyApplicationAlreadyExistsException
        }
    }

    override fun checkDaybreakStudyTypeExists(schoolId: UUID, name: String) {
        if (queryDaybreakStudyTypePort.existsDaybreakStudyTypeBySchoolIdAndName(schoolId, name)) {
            throw DaybreakStudyTypeAlreadyExistsException
        }
    }
}
