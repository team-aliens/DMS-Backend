package team.aliens.dms.domain.daybreak.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.UUID

data class ApplyDaybreakStudyApplicationWebRequest(

    @field:NotNull
    val teacherId: UUID,

    @field:NotNull
    val typeId: UUID,

    @field:NotBlank
    @field:Size(max = 200)
    val reason: String,

    // 월~목 중 고른 날짜들. 연속하지 않아도 된다(예: 월·수)
    @field:Size(max = 4)
    val dates: List<LocalDate>? = null,

    // 구간으로 신청하는 구버전 학생 앱 호환용(#1138). 구버전이 사라지면 startDate/endDate를 제거한다
    val startDate: LocalDate? = null,

    val endDate: LocalDate? = null,
) {

    /**
     * dates가 있으면 그대로 쓰고, 없으면 구버전의 startDate~endDate 구간을 날짜 목록으로 펼친다.
     *
     * 빈 목록이 나오면(둘 다 없음, 시작일이 종료일보다 뒤) 도메인 검증에서 DaybreakEmptyDateException이 나간다.
     * 구간은 5일까지만 펼친다 — 5일이면 이미 한 주의 월~목을 벗어나 도메인 검증에서 거절된다.
     */
    fun resolveDates(): List<LocalDate> {
        if (!dates.isNullOrEmpty()) return dates

        val start = startDate ?: return emptyList()
        val end = endDate ?: return emptyList()

        return generateSequence(start) { it.plusDays(1) }
            .takeWhile { it <= end }
            .take(5)
            .toList()
    }
}
