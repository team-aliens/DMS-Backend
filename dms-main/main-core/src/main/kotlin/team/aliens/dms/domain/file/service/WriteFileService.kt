package team.aliens.dms.domain.file.service

import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.student.model.Student

interface WriteFileService {

    fun writeStudentExcelFile(students: List<Student>): ByteArray

    fun writePointHistoryExcelFile(pointHistories: List<PointHistory>): ByteArray

    fun writePointHistoriesExcelFile(pointHistories: List<PointHistory>, students: List<StudentWithTag>): ByteArray

    fun writeRemainStatusExcelFile(studentRemainInfos: List<StudentRemainInfo>): ByteArray

    fun writeDaybreakStudyApplicationExcelFile(applications: List<DaybreakStudyApplicationVO>): ByteArray
}
