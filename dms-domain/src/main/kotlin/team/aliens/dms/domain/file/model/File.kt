package team.aliens.dms.domain.file.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.format.DateTimeFormatter

@Aggregate
class File private constructor() {
    companion object {
        val FILE_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    }
}
