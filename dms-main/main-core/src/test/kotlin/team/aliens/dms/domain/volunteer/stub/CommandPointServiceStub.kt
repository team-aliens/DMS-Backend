package team.aliens.dms.domain.volunteer.stub

import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.service.CommandPointService
import java.util.UUID

abstract class CommandPointServiceStub : CommandPointService {
    override fun savePointHistory(pointHistory: PointHistory): PointHistory =
        throw UnsupportedOperationException()

    override fun deletePointHistory(pointHistory: PointHistory) =
        throw UnsupportedOperationException()

    override fun savePointOption(pointOption: PointOption): PointOption =
        throw UnsupportedOperationException()

    override fun deletePointOption(pointOption: PointOption) =
        throw UnsupportedOperationException()

    override fun saveAllPointHistories(pointHistories: List<PointHistory>, studentIds: List<UUID>?) =
        throw UnsupportedOperationException()
}