package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.point.model.PointHistory

interface WriteFilePort {
    fun writePointHistoryExcelFile(pointHistories: List<PointHistory>): ByteArray
}