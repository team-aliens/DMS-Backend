package team.aliens.dms.persistence.bug.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.domain.bug.model.DevelopmentArea
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.time.LocalDateTime
import java.util.UUID

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

    override val createdAt: LocalDateTime,

    @ElementCollection
    @CollectionTable(name = "tbl_bug_attachment", joinColumns = [JoinColumn(name = "bug_report_id")])
    val attachments: MutableList<BugAttachmentJpaEntity>? = mutableListOf()

) : BaseEntity(id)
