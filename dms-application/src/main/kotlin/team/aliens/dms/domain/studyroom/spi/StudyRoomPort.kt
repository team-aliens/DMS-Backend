package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.manager.spi.ManagerCommandStudyRoomPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentQueryStudyRoomPort

interface StudyRoomPort :
    QueryStudyRoomPort,
    CommandStudyRoomPort,
    SeatTypeQueryStudyRoomPort,
    StudentQueryStudyRoomPort,
    StudentCommandStudyRoomPort,
    ManagerQueryStudyRoomPort,
    ManagerCommandStudyRoomPort
