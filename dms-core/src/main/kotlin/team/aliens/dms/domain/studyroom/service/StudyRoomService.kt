package team.aliens.dms.domain.studyroom.service

import team.aliens.dms.common.annotation.Service

@Service
class StudyRoomService(
    checkStudyRoomService: CheckStudyRoomService,
    getStudyRoomService: GetStudyRoomService,
    commandStudyRoomService: CommandStudyRoomService
) : CheckStudyRoomService by checkStudyRoomService,
    GetStudyRoomService by getStudyRoomService,
    CommandStudyRoomService by commandStudyRoomService
