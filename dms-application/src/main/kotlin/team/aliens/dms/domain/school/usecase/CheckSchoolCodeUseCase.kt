package team.aliens.dms.domain.school.usecase

import team.aliens.dms.domain.school.exception.CodeNotMatchedException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import java.util.*

@ReadOnlyUseCase
class CheckSchoolCodeUseCase(
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(schoolCode: String): UUID {
        val school = querySchoolPort.querySchoolByCode(schoolCode) ?: throw CodeNotMatchedException

        return school.id
    }
}