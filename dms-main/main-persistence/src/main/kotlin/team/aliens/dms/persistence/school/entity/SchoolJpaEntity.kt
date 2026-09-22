package team.aliens.dms.persistence.school.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import team.aliens.dms.persistence.BaseUUIDEntity
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(
    name = "tbl_school",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("name", "address"))
    ]
)
class SchoolJpaEntity(

    id: UUID?,

    @Column(length = 20, nullable = false)
    val name: String,

    @Column(length = 8, nullable = false, unique = true)
    val code: String,

    @Column(length = 100, nullable = false)
    val question: String,

    @Column(length = 100, nullable = false)
    val answer: String,

    @Column(nullable = false)
    val address: String,

    @Column(nullable = false)
    val contractStartedAt: LocalDate,

    val contractEndedAt: LocalDate?

) : BaseUUIDEntity(id)
