package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.StudyRoomAvailableTimeErrorCode

object StudyRoomAvailableTimeNotFoundException : DmsException(
    StudyRoomAvailableTimeErrorCode.STUDY_ROOM_AVAILABLE_TIME_NOT_FOUND
)