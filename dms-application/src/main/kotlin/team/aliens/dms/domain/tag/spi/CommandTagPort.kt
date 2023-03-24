package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.StudentTag

interface CommandTagPort {

    fun saveAllStudentTags(studentTags: List<StudentTag>)

}