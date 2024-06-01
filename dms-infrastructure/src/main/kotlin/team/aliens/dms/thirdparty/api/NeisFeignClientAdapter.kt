package team.aliens.dms.thirdparty.api

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.NeisFeignClientPort
import team.aliens.dms.domain.meal.dto.NeisMealServiceDietInfosResponse
import team.aliens.dms.domain.meal.dto.NeisMealServiceDietInfosResponse.MealServiceDietInfoElement
import team.aliens.dms.domain.school.dto.NeisSchoolResponse
import team.aliens.dms.thirdparty.api.client.NeisFeignClient
import team.aliens.dms.thirdparty.api.client.dto.NeisFeignClientMealServiceDietInfoResponse
import team.aliens.dms.thirdparty.api.client.dto.NeisFeignClientSchoolInfoResponse
import team.aliens.dms.thirdparty.api.client.property.NeisFeignClientRequestProperty
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class NeisFeignClientAdapter(
    @Value("\${open-feign.neis-key}")
    private val neisKey: String,
    private val neisFeignClient: NeisFeignClient
) : NeisFeignClientPort {

    override fun getNeisSchoolInfo(schoolName: String, schoolAddress: String): NeisSchoolResponse {
        runCatching {
            /**
             * HTML 형식의 JSON 을 Gson 을 사용하여 오브젝트로 변환
             */
            val neisSchoolInfoString = neisFeignClient.getSchoolInfo(
                type = NeisFeignClientRequestProperty.TYPE,
                pageIndex = NeisFeignClientRequestProperty.PAGE_INDEX,
                pageSize = NeisFeignClientRequestProperty.PAGE_SIZE,
                schoolName = schoolName,
                schoolAddress = schoolAddress
            )

            val neisSchoolInfoResponse = Gson().fromJson(neisSchoolInfoString, NeisFeignClientSchoolInfoResponse::class.java)

            return NeisSchoolResponse(
                sdSchoolCode = neisSchoolInfoResponse.schoolInfo[1].row.first().SD_SCHUL_CODE,
                regionCode = neisSchoolInfoResponse.schoolInfo[1].row.first().ATPT_OFCDC_SC_CODE
            )
        }.onFailure {
            it.printStackTrace()
        }.getOrThrow()
    }

    /**
     * HTML 형식의 JSON 을 Gson 을 사용하여 오브젝트로 변환
     */
    override fun getNeisMealServiceDietInfo(
        sdSchoolCode: String,
        regionCode: String
    ): NeisMealServiceDietInfosResponse {
        runCatching {
            val nextMonth = LocalDate.now().plusMonths(0)

            val neisMealServiceDietInfoString = neisFeignClient.getMealServiceDietInfo(
                key = neisKey,
                type = NeisFeignClientRequestProperty.TYPE,
                pageIndex = NeisFeignClientRequestProperty.PAGE_INDEX,
                pageSize = NeisFeignClientRequestProperty.PAGE_SIZE,
                sdSchoolCode = sdSchoolCode,
                regionCode = regionCode,
                startedYmd = convertDateTimeFormat(nextMonth.withDayOfMonth(1).toString()),
                endedYmd = convertDateTimeFormat(nextMonth.withDayOfMonth(nextMonth.lengthOfMonth()).toString())
            )

            val mealJson = Gson().fromJson(neisMealServiceDietInfoString, NeisFeignClientMealServiceDietInfoResponse::class.java)
            val mealTotalCount = mealJson.mealServiceDietInfo.first().head.first().list_total_count

            val mealCodes = mutableListOf<String>()
            val mealInfoElements = mutableListOf<MealServiceDietInfoElement>()

            val breakfastMap = mutableMapOf<LocalDate, String>()
            val lunchMap = mutableMapOf<LocalDate, String>()
            val dinnerMap = mutableMapOf<LocalDate, String>()

            for (i: Int in 0 until mealTotalCount) {
                val mealCode = getMealCode(mealJson, i)
                val calInfo = getCalInfo(mealJson, i)
                val menu = getMenuReplace(mealJson, i)
                val mealDate = getMealDate(mealJson, i)

                val transferMealDate = convertDateTimeFormat(mealDate)
                val mealLocalDate = stringToLocalDate(transferMealDate)

                mealCodes.add(
                    index = i,
                    element = mealCode
                )

                val menuAndClaInfo = "$menu||$calInfo"

                when (mealCodes[i]) {
                    "1" -> breakfastMap[mealLocalDate] = menuAndClaInfo
                    "2" -> lunchMap[mealLocalDate] = menuAndClaInfo
                    "3" -> dinnerMap[mealLocalDate] = menuAndClaInfo
                }

                mealInfoElements.add(
                    index = i,
                    element = MealServiceDietInfoElement(
                        mealDate = mealLocalDate,
                        breakfast = breakfastMap[mealLocalDate].orEmpty(),
                        lunch = lunchMap[mealLocalDate].orEmpty(),
                        dinner = dinnerMap[mealLocalDate].orEmpty()
                    )
                )
            }
            return NeisMealServiceDietInfosResponse(mealInfoElements)
        }.onFailure {
            it.printStackTrace()
        }.getOrThrow()
    }

    private fun getRow(response: NeisFeignClientMealServiceDietInfoResponse, i: Int) =
        response.mealServiceDietInfo[1].row[i]

    private fun getMealCode(response: NeisFeignClientMealServiceDietInfoResponse, i: Int) =
        getRow(response, i).MMEAL_SC_CODE

    private fun getCalInfo(response: NeisFeignClientMealServiceDietInfoResponse, i: Int) = getRow(response, i).CAL_INFO

    private fun getMenuReplace(response: NeisFeignClientMealServiceDietInfoResponse, i: Int) =
        getRow(response, i).DDISH_NM
            .replace("<br/>", "||") // <br/>를 ||로 변환
            .replace("/", "&") // /를 &로 변환
            .replace("[0-9.()]".toRegex(), "") // "0 ~ 9", ".", "(", ")" 를 전부 제거하는 정규식
            .replace("\\p{Z}".toRegex(), "") // 공백을 전부 제거하는 정규식

    private fun getMealDate(response: NeisFeignClientMealServiceDietInfoResponse, i: Int) = getRow(response, i).MLSV_YMD

    /**
     * 조건에 맞게 date 형식 변환
     */
    private fun convertDateTimeFormat(date: String) = if (date.length > 8) {
        date.replace("-", "")
    } else {
        val sb = StringBuffer()
        sb.append(date)
        sb.insert(4, "-")
        sb.insert(7, "-")
        sb.toString()
    }

    /**
     * String 타입을 LocalDate 타입 으로 변환
     */
    private fun stringToLocalDate(transferMealDate: String): LocalDate {
        val passer = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        return LocalDate.parse(transferMealDate, passer)
    }
}
