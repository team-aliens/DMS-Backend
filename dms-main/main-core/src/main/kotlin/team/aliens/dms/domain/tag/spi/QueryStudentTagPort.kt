package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO

interface QueryStudentTagPort {

    fun queryAllStudentTagDetails(): List<StudentTagDetailVO>
}
