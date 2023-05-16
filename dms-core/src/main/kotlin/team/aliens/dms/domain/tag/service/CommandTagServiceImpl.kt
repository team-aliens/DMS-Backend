package team.aliens.dms.domain.tag.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.CommandStudentTagPort
import team.aliens.dms.domain.tag.spi.CommandTagPort
import java.util.UUID

@Service
class CommandTagServiceImpl(
    private val commandStudentTagPort: CommandStudentTagPort,
    private val commandTagPort: CommandTagPort
) : CommandTagService {

    override fun deleteStudentTagById(studentId: UUID, tagId: UUID) {
        commandStudentTagPort.deleteStudentTagById(studentId, tagId)
    }

    override fun deleteStudentTagAndTagById(tagId: UUID) {
        commandStudentTagPort.deleteStudentTagByTagId(tagId)
        commandTagPort.deleteTagById(tagId)
    }

    override fun saveTag(tag: Tag): Tag {
        return commandTagPort.saveTag(tag)
    }

    override fun saveAllStudentTags(studentTags: List<StudentTag>) {
        commandStudentTagPort.saveAllStudentTags(studentTags)
    }
}
