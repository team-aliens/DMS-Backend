package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.exception.error.StudyRoomErrorCode

object TimeSlotNotFoundException : DmsException(
    StudyRoomErrorCode.TIME_SLOT_NOT_FOUND
)
