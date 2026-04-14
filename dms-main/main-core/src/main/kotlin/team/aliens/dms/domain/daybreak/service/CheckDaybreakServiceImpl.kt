package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationAlreadyExistsException
import team.aliens.dms.domain.daybreak.spi.QueryDaybreakStudyApplicationPort
import java.util.UUID

@Service
class CheckDaybreakServiceImpl(
    private val queryDaybreakStudyApplicationPort: QueryDaybreakStudyApplicationPort,
) : CheckDaybreakService {

    override fun checkDaybreakStudyApplicationByStudentId(studentId: UUID) {
        if (queryDaybreakStudyApplicationPort.existActiveDaybreakStudyApplicationByStudentId(studentId)) {
            throw DaybreakStudyApplicationAlreadyExistsException
        }
    }
}
