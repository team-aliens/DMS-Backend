package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import java.io.File

interface WriteFilePort {

    fun writeStudentExcelFile(students: List<Student>): ByteArray

    fun writePointHistoryExcelFile(pointHistories: List<PointHistory>): ByteArray

    fun writePointHistoriesExcelFile(pointHistories: List<PointHistory>, students: List<StudentWithTag>): ByteArray

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

    fun writeOutingApplicationExcelFile(outingApplicationExcelVos: List<OutingApplicationVO>): ByteArray
}
