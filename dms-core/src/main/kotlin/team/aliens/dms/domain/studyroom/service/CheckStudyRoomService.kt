package team.aliens.dms.domain.studyroom.service

import java.util.UUID

interface CheckStudyRoomService {

    fun checkStudyRoomExistsByFloorAndName(floor: Int, name: String, schoolId: UUID)

    fun checkStudyRoomTimeSlotExistsById(studyRoomId: UUID, timeSlotId: UUID)

    fun checkStudyRoomApplicationTimeAvailable(schoolId: UUID)
}
