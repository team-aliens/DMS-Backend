package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School

interface CommandSchoolPort {

    fun saveSchool(school: School): School

    fun saveAvailableFeature(availableFeature: AvailableFeature): AvailableFeature
}
