package team.aliens.dms.persistence.bug.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.bug.model.BugAttachment
import team.aliens.dms.domain.bug.model.BugReport
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.bug.entity.BugAttachmentJpaEntity
import team.aliens.dms.persistence.bug.entity.BugReportJpaEntity
import team.aliens.dms.persistence.student.repository.StudentJpaRepository

@Component
class BugReportMapper(
    private val studentRepository: StudentJpaRepository
) : GenericMapper<BugReport, BugReportJpaEntity> {

    override fun toDomain(entity: BugReportJpaEntity?): BugReport? {
        return entity?.let {
            BugReport(
                id = it.id!!,
                studentId = it.student!!.id!!,
                content = it.content,
                type = it.bugType,
                developmentArea = it.developmentArea,
                createdAt = it.createdAt,
                attachmentUrls = BugAttachment(it.attachments?.map { it.attachmentUrl })
            )
        }
    }

    override fun toEntity(domain: BugReport): BugReportJpaEntity {
        val student = studentRepository.findByIdOrNull(domain.studentId)

        val attachments = domain.attachmentUrls?.attachmentUrls?.map { BugAttachmentJpaEntity(it) }?.toMutableList()

        return BugReportJpaEntity(
            id = domain.id,
            student = student,
            content = domain.content,
            bugType = domain.type,
            developmentArea = domain.developmentArea,
            createdAt = domain.createdAt!!,
            attachments = attachments
        )
    }
}
