package team.aliens.dms.domain.file.service

import org.springframework.stereotype.Service
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import java.io.File

@Service
class WriteFileServiceImpl(
    private val writeFilePort: WriteFilePort
) : WriteFileService {

    override fun writeStudentExcelFile(students: List<Student>) =
        writeFilePort.writeStudentExcelFile(students)

    override fun writePointHistoryExcelFile(pointHistories: List<PointHistory>) =
        writeFilePort.writePointHistoryExcelFile(pointHistories)

    override fun writeRemainStatusExcelFile(studentRemainInfos: List<StudentRemainInfo>) =
        writeFilePort.writeRemainStatusExcelFile(studentRemainInfos)

    override fun addStudyRoomApplicationStatusExcelFile(
        baseFile: File,
        timeSlots: List<TimeSlot>,
        studentSeatsMap: Map<Pair<String, String>, StudentSeatInfo>
    ) = writeFilePort.addStudyRoomApplicationStatusExcelFile(baseFile, timeSlots, studentSeatsMap)

    override fun writeStudyRoomApplicationStatusExcelFile(
        timeSlots: List<TimeSlot>,
        studentSeats: List<StudentSeatInfo>
    ) = writeFilePort.writeStudyRoomApplicationStatusExcelFile(timeSlots, studentSeats)

    override fun writeOutingApplicationExcelFile(outingApplicationVos: List<OutingApplicationVO>) =
        writeFilePort.writeOutingApplicationExcelFile(outingApplicationVos)
}
