package team.aliens.dms.common.service.feign

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NeisFeignClientPort
import team.aliens.dms.domain.meal.dto.NeisMealServiceDietInfosResponse
import team.aliens.dms.domain.school.dto.NeisSchoolResponse

@Service
class NeisFeignServiceImpl(
    private val neisFeignClientPort: NeisFeignClientPort
) : NeisFeignService {

    override fun getNeisSchoolInfo(schoolName: String, schoolAddress: String): NeisSchoolResponse {
        return neisFeignClientPort.getNeisSchoolInfo(
            schoolName = schoolName,
            schoolAddress = schoolAddress
        )
    }

    override fun getNeisMealServiceDietInfo(
        sdSchoolCode: String,
        regionCode: String
    ): NeisMealServiceDietInfosResponse {
        return neisFeignClientPort.getNeisMealServiceDietInfo(
            sdSchoolCode = sdSchoolCode,
            regionCode = regionCode
        )
    }
}
