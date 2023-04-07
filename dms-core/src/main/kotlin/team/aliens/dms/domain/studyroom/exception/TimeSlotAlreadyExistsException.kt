package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.exception.error.StudyRoomErrorCode

object TimeSlotAlreadyExistsException : DmsException(
    StudyRoomErrorCode.TIME_SLOT_ALREADY_EXISTS
)
