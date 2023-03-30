package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.StudyRoomErrorCode

object StudyRoomTimeSlotNotFoundException : DmsException(
    StudyRoomErrorCode.STUDY_ROOM_TIME_SLOT_NOT_FOUND
)
