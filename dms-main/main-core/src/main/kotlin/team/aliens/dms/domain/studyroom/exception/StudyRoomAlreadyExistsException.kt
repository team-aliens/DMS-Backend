package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.exception.error.StudyRoomErrorCode

object StudyRoomAlreadyExistsException : DmsException(
    StudyRoomErrorCode.STUDY_ROOM_ALREADY_EXISTS
)
