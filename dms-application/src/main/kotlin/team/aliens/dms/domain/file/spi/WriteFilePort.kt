package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.remain.dto.StudentRemainInfo

interface WriteFilePort {

    fun writePointHistoryExcelFile(pointHistories: List<PointHistory>): ByteArray

    fun writeRemainStatusExcelFile(studentRemainInfos: List<StudentRemainInfo>): ByteArray
}
