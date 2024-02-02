package team.aliens.dms.persistence.tag.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.util.UUID

@Entity
@Table(
    name = "tbl_tag",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("school_id", "name"))
    ]
)
class TagJpaEntity(

    id: UUID?,

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
