package team.aliens.dms.persistence.school.entity

import team.aliens.dms.persistence.BaseUUIDEntity
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "tbl_school",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["name", "code"])
    ]
)
class SchoolEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

    @Column(columnDefinition = "CHAR(6)", nullable = false)
    val code: String,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val question: String,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val answer: String,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val address: String,

    @Column(columnDefinition = "DATE", nullable = false)
    val contractStartedAt: LocalDate,

    @Column(columnDefinition = "DATE")
    val contractEndedAt: LocalDate,

) : BaseUUIDEntity()