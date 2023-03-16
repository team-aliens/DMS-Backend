package team.aliens.dms.persistence.student.entity

import team.aliens.dms.persistence.BaseEntity
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_student_tag")
class StudentTagJpaEntity(

    override val id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", columnDefinition = "BINARY(16)", nullable = false)
    val tag: TagJpaEntity?

) : BaseEntity(id)
