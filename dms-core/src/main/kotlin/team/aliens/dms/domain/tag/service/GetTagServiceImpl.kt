package team.aliens.dms.domain.tag.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.QueryStudentTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO
import java.util.UUID

@Service
class GetTagServiceImpl(
    private val queryTagPort: QueryTagPort,
    private val queryStudentTagPort: QueryStudentTagPort
) : GetTagService {

    override fun getStudentTagsByTagNameIn(names: List<String>): List<StudentTag> =
        queryStudentTagPort.queryStudentTagsByTagNameIn(names)

    override fun getStudentTagsByStudentId(studentId: UUID): List<StudentTag> =
        queryStudentTagPort.queryStudentTagsByStudentId(studentId)

    override fun getAllStudentTagDetails(): List<StudentTagDetailVO> =
        queryStudentTagPort.queryAllStudentTagDetails()
    
    override fun getTagsByTagNameIn(names: List<String>): List<Tag> =
        queryTagPort.queryTagsByTagNameIn(names)

    override fun getTagByName(name: String) =
        queryTagPort.queryTagByName(name) ?: throw TagNotFoundException

    override fun getTagById(tagId: UUID) =
        queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException

    override fun getTagsBySchoolId(schoolId: UUID): List<Tag> {
        return queryTagPort.queryTagsBySchoolId(schoolId)
    }
}
