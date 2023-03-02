package team.aliens.dms.thirdparty.api.client.dto

/**
 *
 * Neis API 에서 호출한 HTML 형식의 JSON 을 Gson 을 사용하여 오브젝트로 변환 시켜줄때 사용하는 객체
 *
 * @author leejeongyoon
 * @date 2022/11/07
 * @version 1.0.0
 */
data class NeisFeignClientSchoolInfoResponse(
    val schoolInfo: List<SchoolInfo>
)

data class SchoolInfo(
    val head: List<NeisSchoolInfoHead>,
    val row: List<NeisSchoolInfoRow>
)

data class NeisSchoolInfoHead(
    val RESULT: NeisSchoolInfoRESULT,
    val list_total_count: Int
)

data class NeisSchoolInfoRESULT(
    val CODE: String,
    val MESSAGE: String
)

data class NeisSchoolInfoRow(
    val ATPT_OFCDC_SC_CODE: String,
    val SD_SCHUL_CODE: String
)
