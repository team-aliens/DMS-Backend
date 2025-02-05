package team.aliens.dms.common.service.feign

import org.springframework.beans.factory.annotation.Configurable
import team.aliens.dms.domain.meal.dto.NeisMealServiceDietInfosResponse
import team.aliens.dms.domain.school.dto.NeisSchoolResponse

interface NeisFeignService {
    fun getNeisSchoolInfo(schoolName: String, schoolAddress: String): NeisSchoolResponse

    fun getNeisMealServiceDietInfo(sdSchoolCode: String, regionCode: String): NeisMealServiceDietInfosResponse
}
