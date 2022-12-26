package team.aliens.dms.domain.studyroom.spi

interface StudyRoomPort:
    QueryStudyRoomPort,
    CommandStudyRoomPort,
    SeatTypeQueryStudyRoomPort {
}