package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface ManagerQuerySchoolPort {
    fun queryById(id: UUID): School?
}