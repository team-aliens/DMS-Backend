package team.aliens.dms.domain.manager.spi.vo

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

open class StudentWithTag(
    val id: UUID,
    open val name: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val roomNumber: String,
    val profileImageUrl: String,
    val sex: Sex,
    val tags: List<Tag>
) {
    val gcn: String = Student.processGcn(this.grade, this.classRoom, this.number)
}
