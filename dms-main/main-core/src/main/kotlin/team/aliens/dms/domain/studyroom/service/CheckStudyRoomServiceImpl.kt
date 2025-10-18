package team.aliens.dms.domain.studyroom.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.studyroom.exception.AvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.exception.SeatCanNotAppliedException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAlreadyExistsException
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import java.time.LocalTime
import java.util.UUID

@Service
class CheckStudyRoomServiceImpl(
    private val queryStudyRoomPort: QueryStudyRoomPort
) : CheckStudyRoomService {

    override fun checkStudyRoomExistsByFloorAndName(floor: Int, name: String, schoolId: UUID) {
        val isAlreadyExists = queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(
            floor = floor,
            name = name,
            schoolId = schoolId
        )
        if (isAlreadyExists) {
            throw StudyRoomAlreadyExistsException
        }
    }

    override fun checkStudyRoomTimeSlotExistsById(studyRoomId: UUID, timeSlotId: UUID) {
        if (!queryStudyRoomPort.existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(
                studyRoomId,
                timeSlotId
            )
        ) {
            throw StudyRoomTimeSlotNotFoundException
        }
    }

    override fun checkStudyRoomApplicationTimeAvailable(schoolId: UUID) {
        val availableTime = queryStudyRoomPort.queryAvailableTimeBySchoolId(schoolId) ?: throw AvailableTimeNotFoundException
        if (!availableTime.isAvailable(LocalTime.now())) {
            throw SeatCanNotAppliedException
        }
    }
}
