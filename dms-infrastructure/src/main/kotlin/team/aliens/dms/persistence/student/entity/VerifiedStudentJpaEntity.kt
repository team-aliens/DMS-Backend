package team.aliens.dms.persistence.student.entity

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.persistence.BaseUUIDEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "tbl_verified_student",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("schoolName", "gcn"))
    ]
)
class VerifiedStudentJpaEntity(

    override val id: UUID?,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val schoolName: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val name: String,

    @Column(columnDefinition = "VARCHAR(4)", nullable = false)
    val roomNumber: String, // TODO: int로 바꾸기

    @Column(columnDefinition = "CHAR(1)", nullable = false)
    val roomLocation: String,

    @Column(columnDefinition = "VARCHAR(4)", nullable = false)
    val gcn: String,

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: Sex,

    ) : BaseUUIDEntity(id)
