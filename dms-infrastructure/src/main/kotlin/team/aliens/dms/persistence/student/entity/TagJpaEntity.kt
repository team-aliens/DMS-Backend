package team.aliens.dms.persistence.student.entity

import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "tbl_tag",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("school_id", "name"))
    ]
)
class TagJpaEntity(

    override val id: UUID?,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

    @Column(columnDefinition = "CHAR(7)", nullable = false)
    val color: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?

) : BaseUUIDEntity(id)

@Embeddable
data class StudentTagId(

    @Column(nullable = false)
    val studentId: UUID,

    @Column(nullable = false)
    val tagId: UUID

) : Serializable
