package team.aliens.dms.thirdparty.api.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import team.aliens.dms.thirdparty.api.client.property.NeisFeignClientParameterProperty

@FeignClient(name = "neis-feign-client", url = "https://open.neis.go.kr/hub")
interface NeisFeignClient {

    @GetMapping("/schoolInfo")
    fun getSchoolInfo(
        @RequestParam(name = NeisFeignClientParameterProperty.TYPE) type: String,
        @RequestParam(name = NeisFeignClientParameterProperty.PAGE_INDEX) pageIndex: Int,
        @RequestParam(name = NeisFeignClientParameterProperty.PAGE_SIZE) pageSize: Int,
        @RequestParam(name = NeisFeignClientParameterProperty.SCHOOL_NAME) schoolName: String,
        @RequestParam(name = NeisFeignClientParameterProperty.SCHOOL_ADDRESS) schoolAddress: String
    ): String

    @GetMapping("/mealServiceDietInfo")
    fun getMealServiceDietInfo(
        @RequestParam(name = NeisFeignClientParameterProperty.KEY) key: String,
        @RequestParam(name = NeisFeignClientParameterProperty.TYPE) type: String,
        @RequestParam(name = NeisFeignClientParameterProperty.PAGE_INDEX) pageIndex: Int,
        @RequestParam(name = NeisFeignClientParameterProperty.PAGE_SIZE) pageSize: Int,
        @RequestParam(name = NeisFeignClientParameterProperty.SD_SCHOOL_CODE) sdSchoolCode: String,
        @RequestParam(name = NeisFeignClientParameterProperty.REGION_CODE) regionCode: String,
        @RequestParam(name = NeisFeignClientParameterProperty.STARTED_YMD) startedYmd: String,
        @RequestParam(name = NeisFeignClientParameterProperty.ENDED_YMD) endedYmd: String
    ): String
}
