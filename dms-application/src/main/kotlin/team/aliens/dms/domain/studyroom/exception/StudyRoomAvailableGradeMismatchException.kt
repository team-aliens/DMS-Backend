package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.StudyRoomErrorCode

object StudyRoomAvailableGradeMismatchException : DmsException(
    StudyRoomErrorCode.STUDY_ROOM_AVAILABLE_GRADE_MISMATCH
)