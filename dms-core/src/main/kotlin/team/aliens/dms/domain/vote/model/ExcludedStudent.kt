package team.aliens.dms.domain.vote.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.util.UUID

@Aggregate
data class ExcludedStudent(

    val id: UUID,

    val studentId: UUID,

    override val schoolId: UUID

) : SchoolIdDomain
