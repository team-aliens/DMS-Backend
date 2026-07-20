package team.aliens.dms.domain.file.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.student.model.Student

@Service
class WriteFileServiceImpl(
    private val writeFilePort: WriteFilePort
) : WriteFileService {

    override fun writeStudentExcelFile(students: List<Student>) =
        writeFilePort.writeStudentExcelFile(students)

    override fun writePointHistoryExcelFile(pointHistories: List<PointHistory>) =
        writeFilePort.writePointHistoryExcelFile(pointHistories)

    override fun writePointHistoriesExcelFile(pointHistories: List<PointHistory>, students: List<StudentWithTag>) =
        writeFilePort.writePointHistoriesExcelFile(pointHistories, students)

    override fun writeRemainStatusExcelFile(studentRemainInfos: List<StudentRemainInfo>) =
        writeFilePort.writeRemainStatusExcelFile(studentRemainInfos)

    override fun writeDaybreakStudyApplicationExcelFile(applications: List<DaybreakStudyApplicationVO>) =
        writeFilePort.writeDaybreakStudyApplicationExcelFile(applications)
}
