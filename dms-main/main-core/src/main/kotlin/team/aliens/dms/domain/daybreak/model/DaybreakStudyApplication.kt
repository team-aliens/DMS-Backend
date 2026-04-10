package team.aliens.dms.domain.daybreak.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class DaybreakStudyApplication(

    val id: UUID,

    val studyTypeId: UUID,

    val startDate: LocalDate,

    val endDate: LocalDate,

    val reason: String,

    val status: Status,

    val teacherId: UUID,

    val studentId: UUID,

    override val schoolId: UUID

) : SchoolIdDomain
