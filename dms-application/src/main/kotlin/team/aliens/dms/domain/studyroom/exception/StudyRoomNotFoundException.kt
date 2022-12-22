package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.StudyRoomErrorCode

object StudyRoomNotFoundException : DmsException(
    StudyRoomErrorCode.STUDY_ROOM_NOT_FOUND
)