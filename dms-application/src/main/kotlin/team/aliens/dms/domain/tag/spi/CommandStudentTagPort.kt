package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.StudentTag

interface CommandStudentTagPort {

    fun saveAllStudentTags(studentTags: List<StudentTag>)

}