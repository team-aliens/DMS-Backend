package team.aliens.dms.domain.school

import team.aliens.dms.domain.school.exception.SchoolMismatchException
import java.util.UUID

fun validateSameSchool(sId1: UUID, sId2: UUID) {
    if (sId1 != sId2) {
        throw SchoolMismatchException
    }
}
