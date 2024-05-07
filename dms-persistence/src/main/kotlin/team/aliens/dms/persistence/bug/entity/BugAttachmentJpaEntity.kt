package team.aliens.dms.persistence.bug.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class BugAttachmentJpaEntity(

    @Column(columnDefinition = "VARCHAR(300)")
    val attachmentUrl: String
)
