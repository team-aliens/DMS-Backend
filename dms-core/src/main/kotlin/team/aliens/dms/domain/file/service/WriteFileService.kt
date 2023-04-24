package team.aliens.dms.domain.file.service

import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import java.io.File

interface WriteFileService {

    fun writePointHistoryExcelFile(pointHistories: List<PointHistory>): ByteArray

    fun writeRemainStatusExcelFile(studentRemainInfos: List<StudentRemainInfo>): ByteArray

    fun addStudyRoomApplicationStatusExcelFile(
        baseFile: File,
        timeSlots: List<TimeSlot>,
        studentSeatsMap: Map<Pair<String, String>, StudentSeatInfo>
    ): ByteArray

    fun writeStudyRoomApplicationStatusExcelFile(
        timeSlots: List<TimeSlot>,
        studentSeats: List<StudentSeatInfo>
    ): ByteArray
}
