package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.outing.dto.response.OutingApplicationHistoriesResponse
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import java.time.LocalDate

@ReadOnlyUseCase
class GetOutingApplicationHistoriesUseCase(
    private val outingService: OutingService,
) {

    fun execute(studentName: String?, date: LocalDate): OutingApplicationHistoriesResponse {

        val outingApplications = outingService.getAllOutingApplicationVOsBetweenStartAndEnd(date, date)

        val outingApplicationInfoSet = outingApplications.flatMap { outingApplication ->
            val outingApplicationInfoList = mutableListOf(
                OutingHistoryVO(
                    outingApplication.id,
                    outingApplication.studentGcn,
                    outingApplication.studentName,
                    outingApplication.outingType,
                    outingApplication.outingTime,
                    outingApplication.arrivalTime,
                    outingApplication.isApproved,
                    outingApplication.isComeback
                )
            )

            for (outingCompanions in outingApplication.outingCompanionVOs)
                if (outingCompanions.studentGcn.isNotBlank())
                    outingApplicationInfoList.add(
                        OutingHistoryVO(
                            outingApplication.id,
                            outingCompanions.studentGcn,
                            outingCompanions.studentName,
                            outingApplication.outingType,
                            outingApplication.outingTime,
                            outingApplication.arrivalTime,
                            outingApplication.isApproved,
                            outingApplication.isComeback
                        )
                    )
            outingApplicationInfoList
        }

        return OutingApplicationHistoriesResponse(outingApplicationInfoSet)
    }
}
