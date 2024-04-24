package team.aliens.dms.persistence.bug.entity

import jakarta.persistence.*
import team.aliens.dms.domain.bug.model.BugType
import team.aliens.dms.domain.bug.model.DevelopmentArea
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "tbl_bug_report")
class BugReportJpaEntity(
    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    @Column(columnDefinition = "VARCHAR(300)", nullable = false)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(7)", nullable = false)
    val developmentArea: DevelopmentArea,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(11)", nullable = false)
    val bugType: BugType,

    override val createdAt: LocalDateTime,

    @ElementCollection
    @CollectionTable(name = "tbl_bug_attachment", joinColumns = [JoinColumn(name = "bug_report_id")])
    val attachments: MutableList<BugAttachmentJpaEntity>? = mutableListOf()

) : BaseEntity(id)
